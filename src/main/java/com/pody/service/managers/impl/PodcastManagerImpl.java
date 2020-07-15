package com.pody.service.managers.impl;

import java.io.*;
import java.util.*;
import java.net.URL;

import com.pody.model.*;
import org.w3c.dom.Node;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.net.InetAddress;

import com.pody.repository.*;

import java.net.URLConnection;

import com.pody.dto.responses.*;
import org.xml.sax.SAXException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.pody.dto.repositories.*;
import org.modelmapper.ModelMapper;

import java.nio.file.StandardCopyOption;

import com.pody.dto.requests.RssDataDto;
import com.pody.service.ErrorJsonHandler;
import com.pody.service.PasswordEncoding;

import javax.xml.parsers.DocumentBuilder;

import org.springframework.http.HttpStatus;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Sort;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.requests.PodcastCreateDto;
import com.pody.dto.requests.StringRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import javax.xml.parsers.DocumentBuilderFactory;

import com.pody.dto.repositories.PodcastListDto;
import com.pody.service.managers.PodcastManager;
import org.springframework.data.domain.PageRequest;

import javax.xml.parsers.ParserConfigurationException;

import static java.util.stream.Collectors.toCollection;

import org.springframework.web.multipart.MultipartFile;

import static java.util.stream.Collectors.collectingAndThen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PodcastManagerImpl implements PodcastManager {
    private PodcastCategoryRepository podcastCategoryRepository;
    private PodcastHashtagRepository podcastHashtagRepository;
    private UserCategoryRespository userCategoryRespository;
    private ListenLaterRepository listenLaterRepository;
    private PodcastViewRepository podcastViewRepository;
    private PodcastLikeRepository podcastLikeRepository;
    private CategoryRepository categoryRepository;
    private PodcastRepository podcastRepository;
    private HashtagRepository hashtagRepository;
    private HistoryRepository historyRepository;
    private CommentRepository commentRepository;
    private BlogRepository blogRepository;
    private NewsRepository newsRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public PodcastManagerImpl(PodcastCategoryRepository podcastCategoryRepository, PodcastHashtagRepository podcastHashtagRepository,
                              UserCategoryRespository userCategoryRespository, ListenLaterRepository listenLaterRepository,
                              PodcastViewRepository podcastViewRepository, PodcastLikeRepository podcastLikeRepository, CategoryRepository categoryRepository,
                              PodcastRepository podcastRepository, HashtagRepository hashtagRepository,
                              HistoryRepository historyRepository, CommentRepository commentRepository, BlogRepository blogRepository, NewsRepository newsRepository,
                              UserRepository userRepository, ModelMapper modelMapper) {
        this.podcastCategoryRepository = podcastCategoryRepository;
        this.podcastHashtagRepository = podcastHashtagRepository;
        this.userCategoryRespository = userCategoryRespository;
        this.listenLaterRepository = listenLaterRepository;
        this.podcastViewRepository = podcastViewRepository;
        this.podcastLikeRepository = podcastLikeRepository;
        this.categoryRepository = categoryRepository;
        this.podcastRepository = podcastRepository;
        this.hashtagRepository = hashtagRepository;
        this.historyRepository = historyRepository;
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
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
                            if (dto.getPodcastCategory().getSubCategory().getId() == null) {
                                dto.getPodcastCategory().setSubCategory(null);
                            }
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
                        filePath = "http://loacalhost/coverImages/" + StringUtils.cleanPath(podcastId.toString() + ".jpg");
                    } catch (IOException e) {
                        return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    int resultImage = podcastRepository.updateImageAddress(filePath, podcastId);

                    //audio upload
                    int resultAudio = 0;
                    if (audio != null) {
                        String filePathAudio = "";
                        String audioDuration = "";
                        Path copyLocation;
                        try {
                            copyLocation = Paths
                                    .get("C:/xampp/htdocs/podcast" + File.separator + StringUtils.cleanPath(podcastId.toString() + ".mp3"));
                            Files.copy(audio.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
                            filePathAudio = "http://localhost/podcast/" + StringUtils.cleanPath(podcastId.toString() + ".mp3");
                        } catch (IOException e) {
                            return new ResponseEntity(ErrorJsonHandler.IO_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                        //Duration Calculator

                        audioDuration = String.valueOf(audio.getSize());

                        resultAudio = podcastRepository.updateAudioAddress(filePathAudio, audioDuration, podcastId);
                    } else {
                        return new ResponseEntity(ErrorJsonHandler.EMPTY_FILE, HttpStatus.BAD_REQUEST);
                    }

                    if (resultImage == 1 && resultAudio == 1) {
                        PodcastReadDto finalPodcast = podcastRepository.readPodcastUpload(podcastId);
                        return ResponseEntity.ok(finalPodcast);
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
    public ResponseEntity updateViewCount(TwoIDRequestDto dto) {
        try {
            //first id is for podcast id
            //second is for logined user id
            if (dto != null) {
                int result = podcastRepository.updateViewCount(dto.getFirstID());
                Podcast findedPodcast = podcastRepository.findOneById(dto.getFirstID());
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
                    if (dto.getSecondID() != null) {
                        History h = new History();
                        h.setPodcast(findedPodcast);
                        h.setUser(new User(dto.getSecondID()));
                        h.setCreatedDate(new Date());
                        History userHistory = historyRepository.save(h);
                        if (userHistory != null) {
                            return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                        } else {
                            return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                        }
                    } else {
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity updateLikeCount(TwoIDRequestDto dto) {
        try {
            //first id is for podcast id
            //second id is for user id
            if (dto != null) {
                int result = podcastRepository.updateLikesCount(dto.getFirstID());
                if (result == 1) {
                    PodcastLike pl = new PodcastLike();
                    pl.setId(null);
                    pl.setCreatedDate(new Date());
                    pl.setPodcast(new Podcast(dto.getFirstID()));
                    pl.setUser(new User(dto.getSecondID()));
                    PodcastLike podcastLike = podcastLikeRepository.save(pl);
                    if (podcastLike != null) {
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity updateDisLikeCount(TwoIDRequestDto dto) {
        try {
            //first id is for podcast id
            //second id is for user id
            if (dto != null) {
                int result = podcastRepository.updateDisLikesCount(dto.getFirstID());
                if (result == 1) {
                    int deleteResult = podcastLikeRepository.deletePodcastLike(dto.getFirstID(), dto.getSecondID());
                    if (deleteResult == 1) {
                        return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                    } else {
                        return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                    }
                } else {
                    return new ResponseEntity(ErrorJsonHandler.NOT_SUCCESSFUL, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity userLikeCheck(TwoIDRequestDto dto) {
        try {
            //First id is for podcast id
            //Second id is for logined user id
            if (dto != null) {
                List<PodcastLike> result = podcastLikeRepository.checkIsLike(dto.getSecondID(), dto.getFirstID());
                if (result != null && result.size() > 0) {
                    return ResponseEntity.ok(ErrorJsonHandler.TRUE);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.FALSE);
                }
            } else {
                return ResponseEntity.ok(ErrorJsonHandler.EMPTY_BODY);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity listPodcastsEachUser(UUID userId, int till, int to) {
        try {
            //this id is for logined user id which is podcasters id
            // default till is 0 and to is 20
            if (userId != null) {
                List<PodcastListDto> listPodcastUser = podcastRepository.listPodcastEachUser(userId, PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "createdDate")));
                return ResponseEntity.ok(listPodcastUser);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_ID_FIELD, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity listPodcastsMostLiked(int till, int to) {
        try {
            //default till , to must be 0 , 20
            List<PodcastListDto> result = podcastRepository.listMostViewedAndLiked(PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "likeCount")));
            return ResponseEntity.ok(result);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity listPodcastsMostViewed(int till, int to) {
        try {
            //default till , to must be 0 , 20
            List<PodcastListDto> result = podcastRepository.listMostViewedAndLiked(PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "viewCount")));
            return ResponseEntity.ok(result);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listPodcastsSuggested(int till, int to) {
        try {
            //default till , to must be 0 , 20
            String orderBy[] = {"createdDate", "viewCount", "likeCount"};
            List<PodcastListDto> result = podcastRepository.listMostViewedAndLiked(PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, orderBy)));
            return ResponseEntity.ok(result);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override //Tested
    public ResponseEntity listPodcastsNewAdded(int till, int to) {
        try {
            //default till , to must be 0 , 20
            List<PodcastListDto> result = podcastRepository.listMostViewedAndLiked(PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "createdDate")));

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
                        listenLater.setCreatedDate(new Date());
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

            List<PodcastTrendingDto> viewingTrendList = podcastRepository.listLatestReleasedTrending(previousDate, now, PageRequest.of(0, 40, Sort.by(Sort.Direction.DESC, view)));
            List<PodcastTrendingDto> likedTrendList = podcastRepository.listLatestReleasedTrending(previousDate, now, PageRequest.of(0, 40, Sort.by(Sort.Direction.DESC, like)));

            Calendar date1 = Calendar.getInstance();
            date1.add(Calendar.DATE, -1);
            Date yesterday = date1.getTime();
            Date today = new Date();

            List<PodcastTrendingDto> dailyTrendList = podcastRepository.listDailyTrends(yesterday, today, PageRequest.of(0, 25));

            List<PodcastTrendingDto> unionList = new ArrayList<>();
            unionList.addAll(viewingTrendList);
            unionList.addAll(likedTrendList);

            if (dailyTrendList != null || dailyTrendList.size() != 0) {
                unionList.addAll(dailyTrendList);
            }

            List<PodcastTrendingDto> finalList = unionList.stream()
                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(PodcastTrendingDto::getPodcastId))),
                            ArrayList::new));

            List<CategorySearchDto> categories = categoryRepository.listTrendingCategory(PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "p.viewCount")));
//            List<Category> finalCategoryList = categories.stream()
//                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(Category::getId))),
//                            ArrayList::new));

            TrendingResponseDto trd = new TrendingResponseDto();
            if (finalList.size() == 50 || finalList.size() < 50) {
                trd.setPodcasts(finalList);
            } else if (finalList.size() > 50) {
                trd.setPodcasts(finalList.subList(0, 50));
            }
            trd.setCategories(categories);

            return ResponseEntity.ok(trd);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity homePagePodcastList(IdResponseDto dto) {
        try {
            HomePageListDto hpld = new HomePageListDto();

            //Categories section
            List<CategoryParentDto> listParents = categoryRepository.listCategoryParents(Sort.by(Sort.Direction.ASC, "name"));
            hpld.setCategories(listParents);

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
            if (dto.getId() == null) {
                String orderBy[] = {"createdDate", "viewCount", "likeCount"};
                List<PodcastListDto> suggestions = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, orderBy)));
                hpld.setSuggestions(suggestions);
            } else {
                Calendar date1 = Calendar.getInstance();
                date1.add(Calendar.DATE, -30);
                Date lastMonth = date1.getTime();
                Date today = new Date();

                String orderBy[] = {"createdDate", "viewCount", "likeCount"};

                List<PodcastListDto> finalSuggestions = new ArrayList<>();

                List<PodcastListDto> listenLaterList = listenLaterRepository.listenLaterList(dto.getId(), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate")));
                if (listenLaterList != null && listenLaterList.size() > 0) {
                    finalSuggestions.addAll(listenLaterList);
                    List<PodcastListDto> suggestions = podcastRepository.listSuggestionsDate(previousDate, now, dto.getId(), PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, orderBy)));
                    finalSuggestions.addAll(suggestions);
                    List<PodcastListDto> followingPodcasts = podcastRepository.listFollowingPodcastersDate(dto.getId(), lastMonth, today, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdDate")));
                    finalSuggestions.addAll(followingPodcasts);
                } else {
                    List<PodcastListDto> suggestions = podcastRepository.listSuggestionsDate(previousDate, now, dto.getId(), PageRequest.of(0, 7, Sort.by(Sort.Direction.DESC, orderBy)));
                    finalSuggestions.addAll(suggestions);
                    List<PodcastListDto> followingPodcasts = podcastRepository.listFollowingPodcastersDate(dto.getId(), lastMonth, today, PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "createdDate")));
                    finalSuggestions.addAll(followingPodcasts);
                }
                finalSuggestions = finalSuggestions.stream()
                        .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(PodcastListDto::getPodcastId))),
                                ArrayList::new));
                hpld.setSuggestions(finalSuggestions);
            }

            //Following Podcasters New Episodes
            if (dto.getId() != null) {
                Calendar date1 = Calendar.getInstance();
                date1.add(Calendar.DATE, -30);
                Date lastMonth = date1.getTime();
                Date today = new Date();

                List<PodcastListDto> followingPodcasts = podcastRepository.listFollowingPodcastersDate(dto.getId(), lastMonth, today, PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdDate")));
                hpld.setFollowings(followingPodcasts);
            } else {
                hpld.setFollowings(new ArrayList<>());
            }

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
                List<ChannelListenCountDto> finalUsers = new ArrayList<>();
                for (ChannelsListDto cld : users) {
                    ChannelListenCountDto clcd = new ChannelListenCountDto();
                    clcd.setChannelInfo(cld);
                    Integer listenCount = podcastViewRepository.channelListenCount(cld.getId());
                    if (listenCount != null) {
                        clcd.setListenCount(listenCount);
                    } else {
                        clcd.setListenCount(0);
                    }
                    finalUsers.add(clcd);
                }
                hpld.setUsers(finalUsers);
            } else {
                List<ChannelsListDto> users = userRepository.listHomePageUsersLoginedUser(dto.getId(), PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "followerCount")));
                List<ChannelListenCountDto> finalUsers = new ArrayList<>();
                for (ChannelsListDto cld : users) {
                    ChannelListenCountDto clcd = new ChannelListenCountDto();
                    clcd.setChannelInfo(cld);
                    Integer listenCount = podcastViewRepository.channelListenCount(cld.getId());
                    if (listenCount != null) {
                        clcd.setListenCount(listenCount);
                    } else {
                        clcd.setListenCount(0);
                    }
                    finalUsers.add(clcd);
                }
                hpld.setUsers(finalUsers);
            }

            Random rand = new Random();
            int upperbound = listParents.size();
            List<CategoryInfoDto> categoryIntroduction = new ArrayList<>();

            Collections.shuffle(listParents);
            for (int i = 0; i < 5; i++) {
                CategoryParentDto cpd = listParents.get(i);
                CategoryInfoDto cid = new CategoryInfoDto();
                cid.setId(cpd.getId());
                cid.setName(cpd.getName());
                cid.setDescription(cpd.getDescription());
                cid.setImageAddress(cpd.getImageAddress());
                String[] orderby = {"viewCount", "likeCount"};
                List<PodcastListDto> podcasts = podcastRepository.listTopPodcastsEachCategory(cpd.getId(), PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, orderby)));
                cid.setPodcasts(podcasts);
                categoryIntroduction.add(cid);
            }
            hpld.setCategoryIntroduction(categoryIntroduction);

            List<BlogListDto> blogs = blogRepository.listBlogs(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate")));
            hpld.setBlogs(blogs);

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
                    User rssUser = userRepository.findOneByRssUrl(dto.getStringValue());
                    if (rssUser != null) {
                        return ResponseEntity.ok(ErrorJsonHandler.DUPLICATE_USERNAME);
                    }
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
                                username = username.replaceAll("([/.@#$%^&*()<>|\\-])", "");
                                User isUserAvailable = userRepository.findOneByUsername(username);
                                if (isUserAvailable == null) {
                                    creatingUser.setUsername(username);
                                } else {
                                    return ResponseEntity.ok(ErrorJsonHandler.DUPLICATE_USERNAME);
                                }
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
                            if (bio.length() > 255) {
                                creatingUser.setBio(bio.substring(0, 255));
                            } else {
                                creatingUser.setBio(bio);
                            }
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
                            creatingUser.setProfileImageAddress("http://pody.ir/defaultImages/profile.png");
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
                        creatingUser.setCreatedDate(new Date());
                        creatingUser.setUpdateDate(new Date());
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

//                            CharsetDetector detector = new CharsetDetector();
//                            detector.setText(podcastTitle.getBytes());
//                            String textEncoding = detector.detect().getName();
//
//                            byte[] bytes = podcastTitle.getBytes("windows-1252");
//                            String asciiEncodedString = new String(bytes, "utf-8");

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
                        LoginResultResponseDto lrrd = modelMapper.map(podcasterDetail, LoginResultResponseDto.class);
                        return ResponseEntity.ok(lrrd);
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
    public ResponseEntity listFollowingPodcasts(int till, int to, IdResponseDto dto) {
        try {
            //this id is logined UserId
            //till 0 , to 20
            if (dto != null) {
                List<PodcastListDto> result = podcastRepository.listFollowingPodcastersInfinite(dto.getId(), PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "createdDate")));
                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity homePagePodcastListMobile(IdResponseDto dto) {
        try {
            //default data of till and to is 0 and 20 in order
            HomeMobileListDto hpld = new HomeMobileListDto();

            //Categories section
            List<CategoryParentDto> listParents = categoryRepository.listCategoryParents(Sort.by(Sort.Direction.ASC, "name"));
            hpld.setCategories(listParents);


            //Podcasts section
            List<PodcastListDto> listAllPodcasts = new ArrayList<>();
            //Podcasts Suggestions
            if (dto.getId() != null) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE, -7);
                Date previousDate = date.getTime();
                Date now = new Date();

                String orderBy[] = {"createdDate", "viewCount", "likeCount"};
                List<PodcastListDto> suggestionsByDate = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, orderBy)));
                listAllPodcasts.addAll(suggestionsByDate);
                //but its not suggestions
                List<PodcastListDto> suggestions = podcastRepository.listFollowingPodcasters(dto.getId(), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate")));
                listAllPodcasts.addAll(suggestions);

                List<PodcastListDto> listenLaterList = listenLaterRepository.listenLaterList(dto.getId(), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate")));
                listAllPodcasts.addAll(listenLaterList);
            } else {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE, -7);
                Date previousDate = date.getTime();
                Date now = new Date();

                String orderBy[] = {"createdDate", "viewCount", "likeCount"};
                List<PodcastListDto> suggestionsByDate = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, orderBy)));
                listAllPodcasts.addAll(suggestionsByDate);

//                List<PodcastListDto> suggestions = podcastRepository.listSuggestions(dto.getId(), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, orderBy)));
//                listAllPodcasts.addAll(suggestions);
            }
            //Podcasts added in last week
            List<PodcastListDto> latestReleased = podcastRepository.listMostViewedAndLiked(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate")));
            listAllPodcasts.addAll(latestReleased);
            //Podcasts Most Viewed
            List<PodcastListDto> mostViewed = podcastRepository.listMostViewedAndLiked(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "viewCount")));
            listAllPodcasts.addAll(mostViewed);
            //Podcasts Most Liked
            List<PodcastListDto> mostLiked = podcastRepository.listMostViewedAndLiked(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "likeCount")));
            listAllPodcasts.addAll(mostLiked);

            List<PodcastListDto> finalList = listAllPodcasts.stream()
                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(PodcastListDto::getPodcastId))),
                            ArrayList::new));

            if (finalList.size() >= 20) {
                finalList = finalList.subList(0, 20);
                hpld.setPartOnePodcasts(finalList.subList(0, 10));
                hpld.setPartTwoPodcasts(finalList.subList(10, 20));
            } else {
                finalList = finalList.subList(0, finalList.size());
                hpld.setPartOnePodcasts(finalList.subList(0, 10));
                hpld.setPartTwoPodcasts(finalList.subList(10, finalList.size()));
            }

            //News Section
            if (dto.getId() == null) {
                List<NewsListDto> listNews = newsRepository.listNewsMobile(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate")));
                if (listNews.size() == 0) {
                    hpld.setFirstNews(null);
                    hpld.setSecondNews(null);
                } else if (listNews.size() == 1) {
                    hpld.setFirstNews(listNews);
                    hpld.setSecondNews(null);
                } else {
                    hpld.setFirstNews(listNews.subList(0, 1));
                    hpld.setSecondNews(listNews.subList(1, 2));
                }
            } else {
                List<NewsListDto> listNews = newsRepository.listNewsLoginedUserMobile(dto.getId(), PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdDate")));
                if (listNews.size() == 0) {
                    hpld.setFirstNews(null);
                    hpld.setSecondNews(null);
                } else if (listNews.size() == 1) {
                    hpld.setFirstNews(listNews);
                    hpld.setSecondNews(null);
                } else {
                    hpld.setFirstNews(listNews.subList(0, 1));
                    hpld.setSecondNews(listNews.subList(1, 2));
                }
            }

            //User Section
            if (dto.getId() == null) {
                List<ChannelsListDto> users = userRepository.listHomePageUsers(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "followerCount")));
                hpld.setUsers(users);
            } else {
                List<ChannelsListDto> users = userRepository.listHomePageUsersLoginedUser(dto.getId(), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "followerCount")));
                hpld.setUsers(users);
            }

            return ResponseEntity.ok(hpld);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity podcastListenLaterList(int till, int to, IdResponseDto dto) {
        try {
            //default till , to must be 0 , 24
            if (dto.getId() != null) {
                List<PodcastListDto> result = listenLaterRepository.listenLaterList(dto.getId(), PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "createdDate")));

                List<PodcastListDto> finalList = result.stream()
                        .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(PodcastListDto::getPodcastId))),
                                ArrayList::new));
                return ResponseEntity.ok(finalList);
            } else {
                return ResponseEntity.ok(ErrorJsonHandler.EMPTY_BODY);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity podcastListenLaterCheck(TwoIDRequestDto dto) {
        try {
            //First id is for logined User id
            //Second id is for podcast id
            if (dto != null) {
                List<ListenLater> result = listenLaterRepository.checkIsListenLater(dto.getFirstID(), dto.getSecondID());
                if (result != null && result.size() > 0) {
                    return ResponseEntity.ok(ErrorJsonHandler.TRUE);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.FALSE);
                }
            } else {
                return ResponseEntity.ok(ErrorJsonHandler.EMPTY_BODY);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity podcastListenLaterDelete(TwoIDRequestDto dto) {
        try {
            //first id is for logined User and second id is for podcast id
            if (dto.getFirstID() != null && dto.getSecondID() != null) {
                int result = listenLaterRepository.deleteListenLater(dto.getFirstID(), dto.getSecondID());
                if (result == 1) {
                    return ResponseEntity.ok(ErrorJsonHandler.SUCCESSFUL);
                } else {
                    return ResponseEntity.ok(ErrorJsonHandler.NOT_SUCCESSFUL);
                }
            } else {
                return new ResponseEntity(ErrorJsonHandler.EMPTY_BODY, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity homePagePodcastListMobileInfinite(int till, int to, IdResponseDto dto) {
        try {
            //default data of till and to is 0 and 20 in order
            //Podcasts section
            List<PodcastListDto> listAllPodcasts = new ArrayList<>();
            //Podcasts Suggestions
            if (dto.getId() != null) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE, -7);
                Date previousDate = date.getTime();
                Date now = new Date();

                String orderBy[] = {"createdDate", "viewCount", "likeCount"};
                List<PodcastListDto> suggestionsByDate = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(till, to / 2, Sort.by(Sort.Direction.DESC, orderBy)));
                listAllPodcasts.addAll(suggestionsByDate);
                //but its not suggestions
                List<PodcastListDto> suggestions = podcastRepository.listFollowingPodcasters(dto.getId(), PageRequest.of(till, to / 2, Sort.by(Sort.Direction.DESC, "createdDate")));
                listAllPodcasts.addAll(suggestions);

                List<PodcastListDto> listenLaterList = listenLaterRepository.listenLaterList(dto.getId(), PageRequest.of(till, to / 2, Sort.by(Sort.Direction.DESC, "createdDate")));
                listAllPodcasts.addAll(listenLaterList);
            } else {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE, -7);
                Date previousDate = date.getTime();
                Date now = new Date();

                String orderBy[] = {"createdDate", "viewCount", "likeCount"};
                List<PodcastListDto> suggestionsByDate = podcastRepository.listLatestReleased(previousDate, now, PageRequest.of(till, to / 2, Sort.by(Sort.Direction.DESC, orderBy)));
                listAllPodcasts.addAll(suggestionsByDate);

//                List<PodcastListDto> suggestions = podcastRepository.listSuggestions(dto.getId(), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, orderBy)));
//                listAllPodcasts.addAll(suggestions);
            }
            //Podcasts added in last week
            List<PodcastListDto> latestReleased = podcastRepository.listMostViewedAndLiked(PageRequest.of(till, to / 2, Sort.by(Sort.Direction.DESC, "createdDate")));
            listAllPodcasts.addAll(latestReleased);
            //Podcasts Most Viewed
            List<PodcastListDto> mostViewed = podcastRepository.listMostViewedAndLiked(PageRequest.of(till, to / 2, Sort.by(Sort.Direction.DESC, "viewCount")));
            listAllPodcasts.addAll(mostViewed);
            //Podcasts Most Liked
            List<PodcastListDto> mostLiked = podcastRepository.listMostViewedAndLiked(PageRequest.of(till, to / 2, Sort.by(Sort.Direction.DESC, "likeCount")));
            listAllPodcasts.addAll(mostLiked);

            List<PodcastListDto> finalList = listAllPodcasts.stream()
                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(PodcastListDto::getPodcastId))),
                            ArrayList::new));

            if (finalList.size() >= 20) {
                finalList = finalList.subList(0, 20);
            } else {
                finalList = finalList.subList(0, finalList.size());
            }

            return ResponseEntity.ok(finalList);
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listLikedPodcastsEachUser(UUID userId, int till, int to) {
        try {
            //default till , to must be 0 , 24
            if (userId != null) {
                List<PodcastListDto> result = podcastRepository.listLikedEachUser(userId, PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "createdDate")));

                List<PodcastListDto> finalList = result.stream()
                        .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(PodcastListDto::getPodcastId))),
                                ArrayList::new));
                return ResponseEntity.ok(finalList);
            } else {
                return ResponseEntity.ok(ErrorJsonHandler.EMPTY_BODY);
            }
        } catch (NullPointerException e) {
            return new ResponseEntity(ErrorJsonHandler.NULL_POINTER_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity listHistoryEachUser(UUID userId, int till, int to) {
        try {
            //default till , to must be 0 , 20
            if (userId != null) {
                List<PodcastListDto> result = podcastRepository.listHistoryEachUser(userId, PageRequest.of(till, to, Sort.by(Sort.Direction.DESC, "createdDate")));

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.ok(ErrorJsonHandler.EMPTY_BODY);
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