package com.webste.anikethta.dao;

import com.webste.anikethta.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class FilePostgresDataAccessService implements FileDaoInterface {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilePostgresDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertFile(UUID id, File file, byte[] multipartFile, String description) {
        Object[] args = new Object[] {file.getFile_size(), id, description, file.getFname(), multipartFile, file.getVisibility()};
        String sql_query = "INSERT INTO public.website_files (file_size, uuid, description, fname, multipart_file, visibility) VALUES (?, ?, ?, ?, ?, ?);";
        return jdbcTemplate.update(sql_query, args);
    }

    @Override
    public int deleteFileById(UUID uuid) {
        String sql_query = "DELETE FROM public.website_files WHERE uuid = ?";
        return jdbcTemplate.update(sql_query, new Object[] {uuid});
    }

    @Override
    public List<File> selectAllFiles() {
        final String sql_query = "SELECT file_size, uuid, description, fname, multipart_file, visibility FROM public.website_files;";
        List<File> files = jdbcTemplate.query(sql_query, (resultSet, i) -> {
            UUID uuid = UUID.fromString(resultSet.getString("uuid"));
            String name = resultSet.getString("fname");
            String description = resultSet.getString("description");
            byte[] multipart_file = resultSet.getBytes("multipart_file");
            Optional<Boolean> visibility;

            try {
                visibility = Optional.of(resultSet.getBoolean("visibility"));
            } catch (NullPointerException e) {
                visibility = Optional.of(true);
            }

            return new File(uuid, name, description, multipart_file, visibility);
        });

        return files;
    }

    @Override
    public Optional<File> selectFileByID(UUID uuid) {
        final String sql_query = "SELECT file_size, uuid, description, fname, multipart_file, visibility FROM public.website_files WHERE uuid=?;";
        File files = jdbcTemplate.queryForObject(sql_query, new Object[]{uuid}, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("uuid"));
            String name = resultSet.getString("fname");
            String description = resultSet.getString("description");
            byte[] multipart_file = resultSet.getBytes("multipart_file");
            Optional<Boolean> visibility;

            try {
                visibility = Optional.of(resultSet.getBoolean("visibility"));
            } catch (NullPointerException e) {
                visibility = Optional.of(true);
            }

            return new File(uuid, name, description, multipart_file, visibility);
        });

        return Optional.ofNullable(files);
    }
}
