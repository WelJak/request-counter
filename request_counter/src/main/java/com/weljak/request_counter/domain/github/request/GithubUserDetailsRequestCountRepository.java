package com.weljak.request_counter.domain.github.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GithubUserDetailsRequestCountRepository extends JpaRepository<GithubGetUserDetailsRequestCount, String> {
    Optional<GithubGetUserDetailsRequestCount> findByLogin(String login);

    void deleteByLogin(String login);
}
