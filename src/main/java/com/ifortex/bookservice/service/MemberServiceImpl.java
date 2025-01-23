package com.ifortex.bookservice.service;

import com.ifortex.bookservice.model.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    private final JdbcTemplate jdbcTemplate;

    public MemberServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member findMember() {
        String sql = "SELECT * " +
                "FROM members m " +
                "JOIN member_books mb ON m.id = mb.member_id " +
                "JOIN books b ON mb.book_id = b.id " +
                "WHERE 'Romance' = ANY(b.genre) " +
                "ORDER BY b.publication_date ASC, m.membership_date DESC LIMIT 1";

        return jdbcTemplate.queryForObject(sql, new MemberRowMapper());
    }

    @Override
    public List<Member> findMembers() {
        String sql = "SELECT * " +
                "FROM members " +
                "WHERE EXTRACT(YEAR FROM membership_date) = 2023 " +
                "AND id NOT IN (SELECT DISTINCT member_id FROM member_books)";

        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            Member member = new Member();
            member.setId(rs.getInt("id"));
            member.setName(rs.getString("name"));
            member.setMembershipDate(rs.getTimestamp("membership_date").toLocalDateTime());
            return member;
        }
    }
}
