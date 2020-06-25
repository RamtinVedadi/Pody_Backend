package com.pody.service;

import com.pody.dto.repositories.CategorySitemapDto;
import com.pody.dto.repositories.PodcastSitemapDto;
import com.pody.dto.repositories.UserSitemapDto;
import com.pody.repository.CategoryRepository;
import com.pody.repository.PodcastRepository;
import com.pody.repository.UserRepository;
import com.redfin.sitemapgenerator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private static final String WEB_PAGE_URL = "http://pody.ir";
    private static final String LOCAL_FOLDER_PATH = "C:\\Sitemap\\";

    //    @Scheduled(cron = "0 0 4 ? * FRI", zone = "Asia/Tehran")
    @Scheduled(cron = "0 20 17 ? * *", zone = "Asia/Tehran")
    public void SitemapPodcastsUpdate() {
        try {
            Integer countEpisodes = podcastRepository.countEpisodes();
            int pageSizes = countEpisodes / 2500;
            for (int i = 0; i <= pageSizes; i++) {
                WebSitemapGenerator webSitemapGenerator = WebSitemapGenerator.builder(WEB_PAGE_URL, new File(LOCAL_FOLDER_PATH)).fileNamePrefix("podcasts_" + i)
                        .build();

                double priority = 0.8;
                ChangeFreq changeFrequency = ChangeFreq.WEEKLY;

                List<PodcastSitemapDto> podcasts = podcastRepository.listPodcastsSitemap(PageRequest.of(i, 2500, Sort.by(Sort.Direction.DESC, "createdDate")));
                for (PodcastSitemapDto psd : podcasts) {
                    WebSitemapUrl sitemapUrl = new WebSitemapUrl.Options("http://pody.ir/play/" + psd.getId()).lastMod(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(psd.getCreatedDate())).priority(priority)
                            .changeFreq(changeFrequency).build();

                    webSitemapGenerator.addUrl(sitemapUrl);
                }

                webSitemapGenerator.write();
            }
        } catch (MalformedURLException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 22 17 ? * *", zone = "Asia/Tehran")
    public void SitemapCategoriesUpdate() {
        try {
            WebSitemapGenerator webSitemapGenerator = WebSitemapGenerator.builder(WEB_PAGE_URL, new File(LOCAL_FOLDER_PATH)).fileNamePrefix("category")
                    .build();

            double priority = 0.8;
            ChangeFreq changeFrequency = ChangeFreq.MONTHLY;

            List<CategorySitemapDto> categories = categoryRepository.listCategorySitemap(PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "createdDate")));
            for (CategorySitemapDto csd : categories) {
                WebSitemapUrl sitemapUrl = new WebSitemapUrl.Options("http://pody.ir/categoryPage/" + csd.getId()).lastMod(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(csd.getCreatedDate())).priority(priority)
                        .changeFreq(changeFrequency).build();

                webSitemapGenerator.addUrl(sitemapUrl);
            }

            webSitemapGenerator.write();

        } catch (MalformedURLException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "30 22 17 ? * *", zone = "Asia/Tehran")
    public void SitemapUsersUpdate() {
        try {
            WebSitemapGenerator webSitemapGenerator = WebSitemapGenerator.builder(WEB_PAGE_URL, new File(LOCAL_FOLDER_PATH)).fileNamePrefix("publishers")
                    .build();

            double priority = 0.8;
            ChangeFreq changeFrequency = ChangeFreq.WEEKLY;

            List<UserSitemapDto> users = userRepository.listUserSitemap();
            for (UserSitemapDto usd : users) {
                WebSitemapUrl sitemapUrl;
                if (usd.getCreatedDate() == null) {
                    sitemapUrl = new WebSitemapUrl.Options("http://pody.ir/publisher/" + usd.getId())
                            .priority(priority).changeFreq(changeFrequency).build();
                } else {
                    sitemapUrl = new WebSitemapUrl.Options("http://pody.ir/publisher/" + usd.getId())
                            .lastMod(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(usd.getCreatedDate()))
                            .priority(priority).changeFreq(changeFrequency).build();
                }

                webSitemapGenerator.addUrl(sitemapUrl);
            }

            webSitemapGenerator.write();

        } catch (MalformedURLException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 23 17 ? * *", zone = "Asia/Tehran")
    public void SitemapIndexUpdate() {
        try {
            SitemapIndexGenerator sitemapIndexGenerator = new SitemapIndexGenerator(WEB_PAGE_URL, new File(LOCAL_FOLDER_PATH + "sitemap.xml"));

            List<String> finalList = new ArrayList<>();

            Integer countEpisodes = podcastRepository.countEpisodes();
            int pageSizes = countEpisodes / 2500;
            for (int i = 0; i <= pageSizes; i++) {
                String podcastXmls = "http://pody.ir/sitemap/podcasts_" + i + ".xml";
                finalList.add(podcastXmls);
            }
            finalList.add("http://pody.ir/sitemap/category.xml");
            finalList.add("http://pody.ir/sitemap/publishers.xml");

            for (String str : finalList) {
                SitemapIndexUrl siu = new SitemapIndexUrl(str, new Date());
                sitemapIndexGenerator.addUrl(siu);
            }

            sitemapIndexGenerator.write();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
