package com.pody.service.managers.impl;

import com.pody.dto.repositories.*;
import com.pody.dto.requests.PodcastCreateDto;
import com.pody.dto.requests.RssDataDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.HomePageListDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.dto.responses.PodcastReadResultDto;
import com.pody.model.*;
import com.pody.repository.*;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.PasswordEncoding;
import com.pody.service.managers.PodcastManager;
import org.apache.commons.codec.binary.Hex;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
@Transactional
public class PodcastManagerImpl implements PodcastManager {
    private PodcastCategoryRepository podcastCategoryRepository;
    private PodcastHashtagRepository podcastHashtagRepository;
    private UserCategoryRespository userCategoryRespository;
    private ListenLaterRepository listenLaterRepository;
    private PodcastViewRepository podcastViewRepository;
    private CategoryRepository categoryRepository;
    private PodcastRepository podcastRepository;
    private HashtagRepository hashtagRepository;
    private HistoryRepository historyRepository;
    private CommentRepository commentRepository;
    private NewsRepository newsRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public PodcastManagerImpl(PodcastCategoryRepository podcastCategoryRepository, PodcastHashtagRepository podcastHashtagRepository,
                              UserCategoryRespository userCategoryRespository, ListenLaterRepository listenLaterRepository,
                              PodcastViewRepository podcastViewRepository, CategoryRepository categoryRepository,
                              PodcastRepository podcastRepository, HashtagRepository hashtagRepository,
                              HistoryRepository historyRepository, CommentRepository commentRepository, NewsRepository newsRepository,
                              UserRepository userRepository, ModelMapper modelMapper) {
        this.podcastCategoryRepository = podcastCategoryRepository;
        this.podcastHashtagRepository = podcastHashtagRepository;
        this.userCategoryRespository = userCategoryRespository;
        this.listenLaterRepository = listenLaterRepository;
        this.podcastViewRepository = podcastViewRepository;
        this.categoryRepository = categoryRepository;
        this.podcastRepository = podcastRepository;
        this.hashtagRepository = hashtagRepository;
        this.historyRepository = historyRepository;
        this.commentRepository = commentRepository;
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity create(PodcastCreateDto dto) {
        try {
            if (dto != null) {
                if (dto.getPodcast() != null) {
                    if (dto.getPodcastCategory() != null) {
                        dto.getPodcast().setId(null);
                        dto.getPodcast().setCreatedDate(new Date());

                        Podcast result = podcastRepository.save(dto.getPodcast());
                        if (result.getShortDescription().contains("#")) {
                            String[] hashtags = result.getShortDescription().split("#");

                            for (int i = 0; i < hashtags.length; i++) {
                                Hashtag availableHashtag = hashtagRepository.findOneByName(hashtags[i]);
                                if (availableHashtag == null) {
                                    Hashtag h = new Hashtag();
                                    h.setName(hashtags[i]);
                                    h.setCreatedDate(new Date());
                                    h.setImageAddress(null);
                                    Hashtag rh = hashtagRepository.save(h);

                                    PodcastHashtag ch = new PodcastHashtag();
                                    ch.setPodcast(result);
                                    ch.setHashtag(rh);
                                    podcastHashtagRepository.save(ch);
                                } else {
                                    PodcastHashtag ch = new PodcastHashtag();
                                    ch.setPodcast(result);
                                    ch.setHashtag(availableHashtag);
                                    podcastHashtagRepository.save(ch);
                                }
                            }
                        }

                        if (result.getDescription().contains("#")) {
                            String[] hashtags = result.getDescription().split("#");

                            for (int i = 0; i < hashtags.length; i++) {
                                Hashtag availableHashtag = hashtagRepository.findOneByName(hashtags[i]);
                                if (availableHashtag == null) {
                                    Hashtag h = new Hashtag();
                                    h.setName(hashtags[i]);
                                    h.setCreatedDate(new Date());
                                    h.setImageAddress(null);
                                    Hashtag rh = hashtagRepository.save(h);

                                    PodcastHashtag ch = new PodcastHashtag();
                                    ch.setPodcast(result);
                                    ch.setHashtag(rh);
                                    podcastHashtagRepository.save(ch);
                                } else {
                                    PodcastHashtag ch = new PodcastHashtag();
                                    ch.setPodcast(result);
                                    ch.setHashtag(availableHashtag);
                                    podcastHashtagRepository.save(ch);
                                }
                            }
                        }

                        if (result != null) {
                            dto.getPodcastCategory().setPodcast(result);
                        }
                        PodcastCategory categoryResults = podcastCategoryRepository.save(dto.getPodcastCategory());

                        if (categoryResults != null) {
                            String json = "{" +
                                    "\"message\":\"SUCCESSFUL\"," +
                                    "\"podcastId\":\"" + result.getId().toString() + "\"" +
                                    "}";
                            return ResponseEntity.ok(json);
                        } else {
                            return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                        }
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity read(UUID id) {
        try {
            if (id != null) {
                PodcastReadResultDto prrd = new PodcastReadResultDto();

                //Podcast Info
                PodcastReadDto podcastInfo = podcastRepository.readPodcast(id);
                prrd.setPodcastInfo(podcastInfo);

                //Comments
                List<PodcastCommentListDto> commentsList = commentRepository.eachPodcastCommentsList(id, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate")));
                if (commentsList != null || commentsList.size() != 0) {
                    prrd.setCommentsList(commentsList);
                } else {
                    prrd.setCommentsList(null);
                }

                //Previous And Next Episodes
                PodcastListDto previousEpisode = null;
                PodcastListDto nextEpisode = null;
                if (podcastInfo.getEpisodeNumber() != null) {
                    int prevEpisodeNum = podcastInfo.getEpisodeNumber() - 1;
                    int nextEpisodeNum = podcastInfo.getEpisodeNumber() + 1;
                    Integer seasonNum = podcastInfo.getSeasonNumber();
                    if (seasonNum != null) {
                        previousEpisode = podcastRepository.previousAndNextPodcastEpisode(prevEpisodeNum, seasonNum, podcastInfo.getUserId());
                        nextEpisode = podcastRepository.previousAndNextPodcastEpisode(nextEpisodeNum, seasonNum, podcastInfo.getUserId());
                    } else {
                        previousEpisode = podcastRepository.previousAndNextPodcastEpisodeWithoutSeason(prevEpisodeNum, podcastInfo.getUserId());
                        nextEpisode = podcastRepository.previousAndNextPodcastEpisodeWithoutSeason(nextEpisodeNum, podcastInfo.getUserId());
                    }
                    if (previousEpisode != null) {
                        prrd.setPreviousEpisode(previousEpisode);
                    } else {
                        prrd.setPreviousEpisode(null);
                    }

                    if (nextEpisode != null) {
                        prrd.setNextEpisode(nextEpisode);
                    } else {
                        prrd.setNextEpisode(null);
                    }
                }

                //Same Category Podcasts
                List<PodcastListDto> sameCategoryPodcasts;
                if (previousEpisode != null && nextEpisode != null) {
                    sameCategoryPodcasts = podcastRepository.listRandomPodcastForEachPodcast(podcastInfo.getCategoryId(), podcastInfo.getPodcastId(), PageRequest.of(0, 8));
                } else if (previousEpisode == null || nextEpisode != null) {
                    sameCategoryPodcasts = podcastRepository.listRandomPodcastForEachPodcast(podcastInfo.getCategoryId(), podcastInfo.getPodcastId(), PageRequest.of(0, 9));
                } else if (previousEpisode != null || nextEpisode == null) {
                    sameCategoryPodcasts = podcastRepository.listRandomPodcastForEachPodcast(podcastInfo.getCategoryId(), podcastInfo.getPodcastId(), PageRequest.of(0, 9));
                } else {
                    sameCategoryPodcasts = podcastRepository.listRandomPodcastForEachPodcast(podcastInfo.getCategoryId(), podcastInfo.getPodcastId(), PageRequest.of(0, 10));
                }

                prrd.setSameCategory(sameCategoryPodcasts);

                return ResponseEntity.ok(prrd);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity update(PodcastCreateDto dto, UUID id) {
        try {
            if (id != null) {
                if (dto.getPodcast() != null) {
                    Podcast dbPodcast = podcastRepository.findOneById(id);
                    dto.getPodcast().setUpdateDate(new Date());
                    modelMapper.map(dto.getPodcast(), dbPodcast);
                    Podcast result = podcastRepository.save(dbPodcast);
                    PodcastCategory resultCategory = null;
                    if (dto.getPodcastCategory() != null) {
                        PodcastCategory dbPodcastCategory = podcastCategoryRepository.findOneById(dto.getPodcastCategory().getId());
                        modelMapper.map(dto.getPodcastCategory(), dbPodcastCategory);
                        resultCategory = podcastCategoryRepository.save(dbPodcastCategory);
                    }
                    if (result != null && resultCategory != null) {
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity delete(UUID id) {
        try {
            if (id != null) {
                Podcast dbPodcast = podcastRepository.findOneById(id);
                podcastCategoryRepository.delete(podcastCategoryRepository.findOneByPodcast(dbPodcast));
                podcastRepository.deleteById(id);
                return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity uploadPodcast(MultipartFile image, MultipartFile audio, UUID podcastId) {
        try {
            if (podcastId != null) {
                if (image != null) {
                    String filePath = "";
                    try {
                        Path copyLocation = Paths
                                .get("C:/xampp/htdocs/coverImages" + File.separator + StringUtils.cleanPath(podcastId.toString() + ".jpg"));
                        Files.copy(image.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                        InetAddress ip = InetAddress.getLocalHost();
                        filePath = "http://192.168.100.10/coverImages/" + StringUtils.cleanPath(podcastId.toString() + ".jpg");
                    } catch (IOException e) {
                        return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    int resultImage = podcastRepository.updateImageAddress(filePath, podcastId);

                    //audio upload
                    int resultAudio = 0;
                    if (audio != null) {
                        String filePathAudio = "";
                        try {
                            Path copyLocation = Paths
                                    .get("C:/xampp/htdocs/podcast" + File.separator + StringUtils.cleanPath(podcastId.toString() + ".mp3"));
                            Files.copy(audio.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                            filePathAudio = "http://192.168.100.10/podcast/" + StringUtils.cleanPath(podcastId.toString() + ".mp3");
                        } catch (IOException e) {
                            return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                        resultAudio = podcastRepository.updateAudioAddress(filePathAudio, podcastId);
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.EMPTY_FILE, HttpStatus.BAD_REQUEST);
                    }

                    if (resultImage == 1 && resultAudio == 1) {
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.EMPTY_FILE, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity updateViewCount(UUID podcastId) {
        try {
            if (podcastId != null) {
                int result = podcastRepository.updateViewCount(podcastId);
                Podcast findedPodcast = podcastRepository.findOneById(podcastId);
                if (result == 1) {
                    Calendar date = Calendar.getInstance();
                    date.add(Calendar.DATE, -1);
                    Date yesterday = date.getTime();
                    Date today = new Date();

                    PodcastView dailyViewExistence = podcastViewRepository.findPodcastView(findedPodcast.getId(), yesterday, today);
                    if (dailyViewExistence == null) {
                        PodcastView pv = new PodcastView();
                        pv.setPodcast(findedPodcast);
                        pv.setDate(new Date());
                        pv.setCount(1);
                        podcastViewRepository.save(pv);
                    } else {
                        podcastViewRepository.updateViewCount(dailyViewExistence.getId());
                    }
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity updateLikeCount(UUID podcastId) {
        try {
            if (podcastId != null) {
                int result = podcastRepository.updateLikesCount(podcastId);
                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity updateDisLikeCount(UUID podcastId) {
        try {
            if (podcastId != null) {
                int result = podcastRepository.updateDisLikesCount(podcastId);
                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity listPodcastsEachUser(UUID userId) {
        try {
            if (userId != null) {
                List<PodcastListDto> listPodcastUser = podcastRepository.listPodcastEachUser(userId, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate")));
                return ResponseEntity.ok(listPodcastUser);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity listPodcastsMostLiked() {
        try {
            List<PodcastListDto> result = podcastRepository.listMostViewedAndLiked(PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "likeCount")));
            return ResponseEntity.ok(result);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity listPodcastsMostViewed() {
        try {
            List<PodcastListDto> result = podcastRepository.listMostViewedAndLiked(PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "viewCount")));
            return ResponseEntity.ok(result);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listPodcastsSuggested() {
        try {
            return ResponseEntity.ok("");
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity listPodcastsNewAdded() {
        try {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, -7);
            Date previousDate = date.getTime();
            Date now = new Date();

            List<PodcastListDto> result = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdDate")));

            return ResponseEntity.ok(result);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity addToListenLater(TwoIDRequestDto dto) {
        try {
            //first id is for podcast id
            //second id is for user id
            if (dto.getFirstID() != null) {
                if (dto.getSecondID() != null) {
                    Podcast dbPodcast = podcastRepository.findOneById(dto.getFirstID());
                    User dbUser = userRepository.findOneById(dto.getSecondID());
                    if (dbPodcast != null && dbUser != null) {
                        ListenLater listenLater = new ListenLater();
                        listenLater.setId(null);
                        listenLater.setPodcast(dbPodcast);
                        listenLater.setUser(dbUser);
                        ListenLater result = listenLaterRepository.save(listenLater);
                        if (result != null) {
                            return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                        } else {
                            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity getRssData(RssDataDto rssDataDto) {
        try {
            if (rssDataDto != null) {
                try {
                    User podcasterDetail = userRepository.findOneById(rssDataDto.getUserId());
                    List<Podcast> userPodcasts = new ArrayList<>();


                    if (podcasterDetail.getRssUrl() == null || podcasterDetail.getRssUrl().equals("")) {
                        int rssResult = userRepository.updateRssUrl(rssDataDto.getRssUrl(), podcasterDetail.getId());
                    }

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    URLConnection connection = new URL(rssDataDto.getRssUrl()).openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
                    Document doc = dBuilder.parse(connection.getInputStream());
                    doc.getDocumentElement().normalize();

                    ArrayList<String> userCategories = new ArrayList<>();
                    NodeList nodeList = doc.getElementsByTagName("itunes:category");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        Element element = (Element) node;
                        String category = element.getAttributes().getNamedItem("text").getTextContent();
                        userCategories.add(category);
                    }

                    ArrayList<String> finalUserCategories = new ArrayList<>(new HashSet<>(userCategories));

                    ArrayList<UserCategory> userCategoryList = new ArrayList<>();
                    for (String str : finalUserCategories) {
                        UserCategory uc = new UserCategory();
                        Category category = categoryRepository.findOneByEnglishName(str);
                        if (category != null) {
                            UserCategory findAllowed = userCategoryRespository.findOneByCategoryAndUser(category, podcasterDetail);
                            if (findAllowed == null) {

                                uc.setId(null);
                                uc.setCategory(category);
                                uc.setUser(podcasterDetail);

                                userCategoryList.add(uc);
                            }
                        }
                    }
                    if (!userCategoryList.isEmpty()) {
                        userCategoryRespository.saveAll(userCategoryList);
                    }

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
                            Podcast pTitle = podcastRepository.findOneByTitleAndUser(podcastTitle, podcasterDetail);
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
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                    }
                } catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
                    e.printStackTrace();
                    return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listTrendingPodcasts() {
        try {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, -30);
            Date previousDate = date.getTime();
            Date now = new Date();

            String view[] = {"createdDate", "viewCount"};
            String like[] = {"createdDate", "likeCount"};

            List<PodcastListDto> viewingTrendList = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(0, 40, Sort.by(Sort.Direction.DESC, view)));
            List<PodcastListDto> likedTrendList = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(0, 40, Sort.by(Sort.Direction.DESC, like)));

            Calendar date1 = Calendar.getInstance();
            date1.add(Calendar.DATE, -1);
            Date yesterday = date1.getTime();
            Date today = new Date();

            List<PodcastListDto> dailyTrendList = podcastRepository.listDailyTrends(yesterday, today, PageRequest.of(0, 25));

            List<PodcastListDto> unionList = new ArrayList<>();
            unionList.addAll(viewingTrendList);
            unionList.addAll(likedTrendList);

            if (dailyTrendList != null || dailyTrendList.size() != 0) {
                unionList.addAll(dailyTrendList);
            }

            List<PodcastListDto> finalList = unionList.stream()
                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(PodcastListDto::getPodcastId))),
                            ArrayList::new));

            return ResponseEntity.ok(finalList);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity homePagePodcastList(IdResponseDto dto) {
        try {
            HomePageListDto hpld = new HomePageListDto();

            //Categories section
            List<Category> listParents = categoryRepository.listCategoryParents(Sort.by(Sort.Direction.ASC, "name"));
            List<CategoryParentListDto> categories = listParents.stream().map(c -> modelMapper.map(c, CategoryParentListDto.class)).collect(Collectors.toList());
            hpld.setCategories(categories);

            //Podcasts section
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, -7);
            Date previousDate = date.getTime();
            Date now = new Date();
            //Podcasts added in last week
            List<PodcastListDto> latestReleased = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdDate")));
            hpld.setLatestReleased(latestReleased);
            //Podcasts Most Viewed
            List<PodcastListDto> mostViewed = podcastRepository.listMostViewedAndLiked(PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "viewCount")));
            hpld.setMostViewed(mostViewed);
            //Podcasts Most Liked
            List<PodcastListDto> mostLiked = podcastRepository.listMostViewedAndLiked(PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "likeCount")));
            hpld.setMostLiked(mostLiked);
            //Podcasts Suggestions
//            if (dto.getId() == null) {
            String orderBy[] = {"createdDate", "viewCount", "likeCount"};
            List<PodcastListDto> suggestions = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, orderBy)));
            hpld.setSuggestions(suggestions);
//            } else {
//                List<PodcastListDto> suggestions = null;
//                hpld.setSuggestions(suggestions);
//            }

            //News Section
            if (dto.getId() == null) {
                List<NewsListDto> listNews = newsRepository.listNews(previousDate, now, PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdDate")));
                hpld.setNews(listNews);
            } else {
                List<NewsListDto> listNews = newsRepository.listNewsLoginedUser(previousDate, now, dto.getId(), PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdDate")));
                hpld.setNews(listNews);
            }

            //User Section
            if (dto.getId() == null) {
                List<ChannelsListDto> users = userRepository.listHomePageUsers(PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "followerCount")));
                hpld.setUsers(users);
            } else {
                List<ChannelsListDto> users = userRepository.listHomePageUsersLoginedUser(dto.getId(), PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "followerCount")));
                hpld.setUsers(users);
            }

            return ResponseEntity.ok(hpld);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity getAllDataWithOnlyRssUrl(StringRequestDto dto) {
        try {
            if (dto != null) {
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    URLConnection connection = new URL(dto.getStringValue()).openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
                    Document doc = dBuilder.parse(connection.getInputStream());
                    doc.getDocumentElement().normalize();

                    User creatingUser = new User();
                    NodeList channelData = doc.getElementsByTagName("channel");
                    for (int i = 0; i < channelData.getLength(); i++) {
                        Node node = channelData.item(i);
                        Element element = (Element) node;
                        creatingUser.setId(null);
                        //User Title And Username
                        try {
                            String userTitle = element.getElementsByTagName("title").item(0).getTextContent();
                            creatingUser.setTitle(userTitle);
                            if (userTitle != "") {
                                String[] splited = userTitle.split(" ");
                                String username = "";
                                for (int j = 0; j < splited.length; j++) {
                                    boolean isArabic = isProbablyArabic(splited[j]);
                                    if (isArabic == false) {
                                        username += splited[j];
                                    }
                                }
                                username = username.replaceAll("([/.@#$%^&*()<>|-])", "");
                                creatingUser.setUsername(username);
                            }
                        } catch (NullPointerException e) {
                            creatingUser.setTitle("");
                        }
                        //Website Url
                        try {
                            String websiteUrl = element.getElementsByTagName("link").item(0).getTextContent();
                            creatingUser.setWebsiteUrl(websiteUrl);
                        } catch (NullPointerException e) {
                            creatingUser.setWebsiteUrl(null);
                        }
                        //Bio
                        try {
                            String bio = element.getElementsByTagName("description").item(0).getTextContent();
                            creatingUser.setBio(bio);
                        } catch (NullPointerException e) {
                            creatingUser.setBio("");
                        }
                        //ProfileImageAddress
                        try {
                            String profileImageAddress = element.getElementsByTagName("itunes:image").item(0).getAttributes().getNamedItem("href").getTextContent();

                            if (profileImageAddress.endsWith("g")) {
                                creatingUser.setProfileImageAddress(profileImageAddress);
                            } else {
                                profileImageAddress += profileImageAddress + "g";
                                creatingUser.setProfileImageAddress(profileImageAddress);
                            }
                        } catch (NullPointerException e) {
                            creatingUser.setProfileImageAddress("http://localhost/profileImages/profile.png");
                        }
                        //User Email
                        try {
                            NodeList nodeList = element.getElementsByTagName("itunes:owner");
                            for (int j = 0; j < nodeList.getLength(); j++) {
                                Node node1 = nodeList.item(i);
                                Element element1 = (Element) node1;
                                String email = element1.getElementsByTagName("itunes:email").item(0).getTextContent();
                                creatingUser.setEmail(email);
                                if (creatingUser.getUsername() == "") {
                                    String[] splitedEmail = email.split("@");
                                    creatingUser.setUsername(splitedEmail[0]);
                                }
                            }
                        } catch (NullPointerException e) {
                            creatingUser.setEmail("");
                        }
                        //User FirstName And LastName
                        try {
                            String fullName = element.getElementsByTagName("itunes:author").item(0).getTextContent();
                            creatingUser.setFirstName(fullName);
                            creatingUser.setLastName("");
                        } catch (NullPointerException e) {
                            creatingUser.setFirstName("");
                            creatingUser.setLastName("");
                        }
                        //User Podcasts Language
                        try {
                            String language = element.getElementsByTagName("language").item(0).getTextContent();
                            creatingUser.setLanguage(language);
                        } catch (NullPointerException e) {
                            creatingUser.setLanguage("");
                        }
                        //User Password
                        PasswordEncoding encoding = new PasswordEncoding();
                        String salt = "Pody123456789";

                        int iterations = 10000;
                        int keyLength = 512;
                        String password = "123456";
                        char[] passwordChars = password.toCharArray();
                        byte[] saltBytes = salt.getBytes();

                        byte[] hashedBytes = encoding.hashPassword(passwordChars, saltBytes, iterations, keyLength);
                        String hashedPassword = Hex.encodeHexString(hashedBytes);
                        creatingUser.setPassword(hashedPassword);
                        //User Rss Url
                        creatingUser.setRssUrl(dto.getStringValue());
                        //Is Channel
                        creatingUser.setIsChannel(true);
                    }

                    User podcasterDetail = userRepository.save(creatingUser);
                    List<Podcast> userPodcasts = new ArrayList<>();

                    ArrayList<String> userCategories = new ArrayList<>();
                    NodeList nodeList = doc.getElementsByTagName("itunes:category");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        Element element = (Element) node;
                        String category = element.getAttributes().getNamedItem("text").getTextContent();
                        userCategories.add(category);
                    }

                    ArrayList<String> finalUserCategories = new ArrayList<>(new HashSet<>(userCategories));

                    ArrayList<UserCategory> userCategoryList = new ArrayList<>();
                    for (String str : finalUserCategories) {
                        UserCategory uc = new UserCategory();
                        Category category = categoryRepository.findOneByEnglishName(str);
                        if (category != null) {
                            UserCategory findAllowed = userCategoryRespository.findOneByCategoryAndUser(category, podcasterDetail);
                            if (findAllowed == null) {

                                uc.setId(null);
                                uc.setCategory(category);
                                uc.setUser(podcasterDetail);

                                userCategoryList.add(uc);
                            }
                        }
                    }
                    if (!userCategoryList.isEmpty()) {
                        userCategoryRespository.saveAll(userCategoryList);
                    }

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
                            Podcast pTitle = podcastRepository.findOneByTitleAndUser(podcastTitle, podcasterDetail);
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
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                    }
                } catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
                    e.printStackTrace();
                    return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }
}
