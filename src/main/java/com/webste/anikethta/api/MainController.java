package com.webste.anikethta.api;


import com.webste.anikethta.model.File;
import com.webste.anikethta.model.FileDisplayJSONObject;
import com.webste.anikethta.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("api/v1")
@RestController
public class MainController {
    private final FileService fileService;

    @Autowired
    public MainController(FileService fileService) {
        this.fileService = fileService;
    }

    // <-------------------------- Basic File Related Services ----------------------------------->
    @PostMapping(path = "/upload")
    public void addFile(@RequestParam(required = false) UUID uuid,
                        @RequestParam("fname") String fname,
                        @RequestParam("file") MultipartFile multipartFile,
                        @RequestParam("description") String description,
                        @RequestParam("visibility") Optional<Boolean> visibility) {

        byte[] file_data;
        try {
            file_data = multipartFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            file_data = null;
        }

        File file = new File(uuid, fname, description, file_data, visibility);
        fileService.addFile(file);
    }

    @GetMapping
    public ResponseEntity<List<FileDisplayJSONObject>> getAllFiles(@RequestParam(required = false) String fname) {
        List<File> all_data = fileService.getAllFiles();
        List<File> filtered_data = new ArrayList<File>();

        for (File unfiltered_file : all_data) {
            if (unfiltered_file.getVisibility()) {
                filtered_data.add(unfiltered_file);
            }
        }

        List<FileDisplayJSONObject> relevant_data = new ArrayList<FileDisplayJSONObject>();

        for (File f : filtered_data) {
            FileDisplayJSONObject fdjo = new FileDisplayJSONObject(f.getFname(),
                    new FileDisplayJSONObject.Info(f.getUuid(), f.getDescription(), f.getFile_size()));

            if (fname == null || fname.isEmpty()) {
                relevant_data.add(fdjo);
            }

            if (fname != null && fname.equals(f.getFname())) {
                relevant_data.add(fdjo);
            }
        }

        return new ResponseEntity<>(relevant_data, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public File getFileByID(@PathVariable("id") UUID uuid) {
        return fileService.getFileById(uuid)
                    .orElse(null);
    }

    @GetMapping(path = "/download/{id}")
    @CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
    public ResponseEntity downloadFile(@PathVariable("id") UUID uuid) {
        File file_2download = fileService.getFileById(uuid)
                .orElse(null);

        if (file_2download == null) return new ResponseEntity("File Not Found", HttpStatus.NOT_FOUND);

        InputStream is = new ByteArrayInputStream(file_2download.getFileData());
        InputStreamResource isr = new InputStreamResource(is);

        HttpHeaders headers = new HttpHeaders();
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + file_2download.getFname() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(isr);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public void deleteFile(@RequestParam("id") String uuid_string) {
        UUID uuid = UUID.fromString(uuid_string);
        fileService.deleteFile(uuid);
    }

    // <------------------------------------------------------------------------------------------>
}
