package com.weljak.request_counter.utils.mapper;

import com.weljak.request_counter.service.external.github.GithubUserDetailsDto;
import com.weljak.request_counter.service.github.GithubUserDetails;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, config = GlobalMapperConfig.class)
public interface GithubApiResponseMapper {
    @Mapping(target = "calculations", source = ".", qualifiedByName = "getCalculations")
    GithubUserDetails toGitHubuserDetails(GithubUserDetailsDto source);

    @Named("getCalculations")
    default String getCalculations(GithubUserDetailsDto source) {
        if (source.getFollowers() <= 0) return null;
        return String.valueOf((6 / source.getFollowers()) * (2 + source.getPublicRepos()));
    }
}
