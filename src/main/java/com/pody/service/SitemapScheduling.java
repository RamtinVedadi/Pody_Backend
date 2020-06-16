package com.pody.service;

import com.pody.repository.CategoryRepository;
import com.pody.repository.PodcastRepository;
import com.pody.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SitemapScheduling {
    private CategoryRepository categoryRepository;
    private PodcastRepository podcastRepository;
    private UserRepository userRepository;

    @Autowired
    public SitemapScheduling(CategoryRepository categoryRepository, PodcastRepository podcastRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.podcastRepository = podcastRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 4 ? * FRI", zone = "Asia/Tehran")
    public void SitemapUpdate() {

    }
}
