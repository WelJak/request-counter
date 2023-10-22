package com.weljak.request_counter.domain.github.request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "github_get_user_details_req_count")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubGetUserDetailsRequestCount {
    @Id
    @Column(nullable = false)
    private String login;

    @Column(name = "req_count", nullable = false)
    private Long requestCount;
}
