package com.pody.service;

import com.pody.dto.repositories.UserRssUpdateDto;
import com.pody.model.Category;
import com.pody.model.Podcast;
import com.pody.model.PodcastCategory;
import com.pody.model.User;
import com.pody.repository.CategoryRepository;
import com.pody.repository.PodcastCategoryRepository;
import com.pody.repository.PodcastRepository;
import com.pody.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Component
public class RssUpdateScheduling {
    private PodcastCategoryRepository podcastCategoryRepository;
    private CategoryRepository categoryRepository;
    private PodcastRepository podcastRepository;
    private UserRepository userRepository;

    @Autowired
    public RssUpdateScheduling(PodcastCategoryRepository podcastCategoryRepository, CategoryRepository categoryRepository,
                               PodcastRepository podcastRepository, UserRepository userRepository) {
        this.podcastCategoryRepository = podcastCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.podcastRepository = podcastRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 30 2 ? * *", zone = "Asia/Tehran")
    public void rssUpdate() {

        List<UserRssUpdateDto> users = userRepository.rssUpdateList();

        for (UserRssUpdateDto u : users) {
            try {
                if (u.getRssUrl() == null || u.getRssUrl().equals("")) {
                    continue;
                } else {
                    try {

                        User podcasterDetail = new User();
                        podcasterDetail.setId(u.getId());
                        podcasterDetail.setRssUrl(u.getRssUrl());
                        podcasterDetail.setProfileImageAddress(u.getProfileImageAddress());

                        List<Podcast> userPodcasts = new ArrayList<>();

                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        URLConnection connection = new URL(u.getRssUrl()).openConnection();
                        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
                        Document doc = dBuilder.parse(connection.getInputStream());
                        doc.getDocumentElement().normalize();

                        String category = doc.getElementsByTagName("itunes:category").item(0).getAttributes().getNamedItem("text").getTextContent();
                        Category mainCategory;
                        Category subCategory;

                        if (category != null) {
                            mainCategory = categoryRepository.findOneByEnglishName(category);
                        } else {
                            mainCategory = null;
                        }

                        Node categoryList = doc.getElementsByTagName("itunes:category").item(1);
                        Element categoryElement = (Element) categoryList;
                        if (categoryElement == null) {
                            subCategory = null;
                        } else {
                            if (categoryElement.getElementsByTagName("itunes:category").item(0) != null) {
                                String childCategory = categoryElement.getElementsByTagName("itunes:category").item(0).getAttributes().getNamedItem("text").getTextContent();
                                subCategory = categoryRepository.findOneByEnglishName(childCategory);
                            } else {
                                subCategory = null;
                            }
                        }

                        NodeList nList = doc.getElementsByTagName("item");
                        for (int temp = 0; temp < nList.getLength(); temp++) {
                            Node nNode = nList.item(temp);

                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;
                                Podcast podcast = new Podcast();

                                //Title
                                String podcastTitle = eElement.getElementsByTagName("title").item(0).getTextContent();
                                List<Podcast> pTitle = podcastRepository.findPodcastsByTitleAndUser(podcastTitle, podcasterDetail);
                                if (pTitle == null) {
                                    podcast.setTitle(podcastTitle);
                                } else {
                                    continue;
                                }

                                //Created Date
                                SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
                                Date createDate = formatter.parse(eElement.getElementsByTagName("pubDate").item(0).getTextContent());
                                podcast.setCreatedDate(createDate);

                                //Image Address
                                try {
                                    String imageAddress = eElement.getElementsByTagName("itunes:image").item(0).getAttributes().getNamedItem("href").getTextContent();

                                    if (imageAddress.endsWith("g")) {
                                        podcast.setImageAddress(imageAddress);
                                    } else {
                                        imageAddress += imageAddress + "g";
                                        podcast.setImageAddress(imageAddress);
                                    }
                                } catch (NullPointerException e) {
                                    podcast.setImageAddress(podcasterDetail.getProfileImageAddress());
                                }

                                //Audio Address
                                try {
                                    String audioAddress = eElement.getElementsByTagName("enclosure").item(0).getAttributes().getNamedItem("url").getTextContent();
                                    podcast.setAudioAddress(audioAddress);
                                } catch (NullPointerException e) {
                                    continue;
                                }

                                //Description
                                try {
                                    String description = eElement.getElementsByTagName("description").item(0).getTextContent();
                                    if (description != null || description != "") {
                                        podcast.setDescription(description);
                                    } else {
                                        podcast.setDescription("");
                                    }
                                } catch (NullPointerException e) {
                                    podcast.setDescription("");
                                }

                                //Short Description
                                try {
                                    String shortDescription = eElement.getElementsByTagName("itunes:summary").item(0).getTextContent();
                                    if (shortDescription.length() > 150) {
                                        shortDescription = shortDescription.substring(0, 149);
                                    }
                                    if (shortDescription != null || shortDescription != "") {
                                        podcast.setShortDescription(shortDescription);
                                    } else {
                                        podcast.setShortDescription("");
                                    }
                                } catch (NullPointerException e) {
                                    podcast.setShortDescription("");
                                }

                                //Duration
                                try {
                                    String duration = eElement.getElementsByTagName("itunes:duration").item(0).getTextContent();
                                    if (duration != null || duration != "") {
                                        podcast.setDuration(duration);
                                    } else {
                                        podcast.setDuration("");
                                    }
                                } catch (NullPointerException e) {
                                    podcast.setDuration("");
                                }

                                //Season Number
                                try {
                                    Integer season = Integer.parseInt(eElement.getElementsByTagName("itunes:season").item(0).getTextContent());
                                    podcast.setSeasonNumber(season);
                                } catch (NullPointerException e) {
                                    podcast.setSeasonNumber(null);
                                }

                                //Episode Number
                                try {
                                    Integer episode = Integer.parseInt(eElement.getElementsByTagName("itunes:episode").item(0).getTextContent());
                                    podcast.setEpisodeNumber(episode);
                                } catch (NullPointerException e) {
                                    podcast.setEpisodeNumber(null);
                                }


                                podcast.setUser(podcasterDetail);

                                userPodcasts.add(podcast);
                            }
                        }

                        List<Podcast> finalList = userPodcasts.stream()
                                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(Podcast::getTitle))),
                                        ArrayList::new));

                        List<Podcast> result = podcastRepository.saveAll(finalList);

                        if (result != null || !result.isEmpty()) {
                            for (Podcast p : result) {
                                PodcastCategory pc = new PodcastCategory();
                                pc.setPodcast(p);
                                pc.setCategory(mainCategory);
                                pc.setSubCategory(subCategory);
                                podcastCategoryRepository.save(pc);
                            }
                        }

                    } catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
