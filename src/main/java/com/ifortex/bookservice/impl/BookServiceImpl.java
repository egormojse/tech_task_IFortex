package com.ifortex.bookservice.impl;

import java.util.Map;

import com.ifortex.bookservice.service.BookService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final JdbcTemplate jdbcTemplate;

    public BookServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, Long> getBooks() {
        String sql = "SELECT UNNEST(genre) AS genre, COUNT(*) AS count " +
                "FROM books " +
                "GROUP BY genre " +
                "ORDER BY count DESC";

        return jdbcTemplate.query(sql, rs -> {
            Map<String, Long> genreCount = new java.util.HashMap<>();
            while (rs.next()) {
                genreCount.put(rs.getString("genre"), rs.getLong("count"));
            }
            return genreCount;
        });
    }
}
