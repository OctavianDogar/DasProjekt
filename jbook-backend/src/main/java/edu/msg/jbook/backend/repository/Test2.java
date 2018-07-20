package edu.msg.jbook.backend.repository;

import edu.msg.jbook.backend.assembler.ProfileAssembler;
import edu.msg.jbook.backend.model.*;
import edu.msg.jbook.backend.service.UserService;
import edu.msg.jbook.backend.util.PasswordEncryptorSHA1;
import edu.msg.jbook.common.NotificationType;
import org.eclipse.persistence.tools.profiler.Profile;
import org.joda.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by dogaro on 04/08/2016.
 */
@Singleton
@Startup
@LocalBean
public class Test2 {

    @PersistenceContext(unitName = "jbook")
    private EntityManager em;

    @EJB
    private UserRepository userRepositoryBean;

    @EJB
    private UserStateRepository userStateRepositoryBean;

    @EJB
    private PostRepository postRepository;

    @EJB
    private EventRepository eventRepository;

    @EJB
    private UserEventRepository userEventRepository;

    @EJB
    private NotificationRepository notificationRepository;

    @EJB
    private UserService userService;

    @EJB
    private ProfileAssembler profileAssembler;

    @PostConstruct
    public void test(){

//        Mr. Admin
// ====================================================================

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");

        admin.setBirthday(new Date(8, 2, 2016));
        admin.setGender(true);
        admin.setUserType(UserType.admin);

        UserPrivacy ap = new UserPrivacy();
        ap.setProfilePrivacy(Privacy.ONLY_ME);
        ap.setContactUser(Privacy.ONLY_ME);
        ap.setSearchByNameOrEmail(Privacy.ONLY_ME);

        admin.setUserPrivacy(ap);

        UserState adminState = new UserState();

        adminState.setEmail("admin@jbook.com");
        adminState.setPassword(PasswordEncryptorSHA1.encryptPassword("admin123456"));
        adminState.setUser(admin);
        adminState.setPhoto("https://dl.dropboxusercontent.com/s/bxjmpkrsa6axu6i/Koala.jpg?dl=0");

        userRepositoryBean.save(admin);
        userStateRepositoryBean.save(adminState);

//        USER 1
// ====================================================================

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        LocalDate local= new LocalDate(2016,8,10);
        user.setBirthday(local.toDate());
        user.setGender(true);
        user.setUserType(UserType.user);

        UserPrivacy up = new UserPrivacy();
        up.setProfilePrivacy(Privacy.FRIENDS);
        up.setContactUser(Privacy.FRIENDS);
        up.setSearchByNameOrEmail(Privacy.FRIENDS_OF_FRIENDS);

        user.setUserPrivacy(up);

        UserState userState = new UserState();

        userState.setEmail("asd@yahoo.com");
        userState.setPassword(PasswordEncryptorSHA1.encryptPassword("123456"));
        userState.setUser(user);
        userState.setPhoto("https://dl.dropboxusercontent.com/s/bxjmpkrsa6axu6i/Koala.jpg?dl=0");

        userRepositoryBean.save(user);
        userStateRepositoryBean.save(userState);

//        USER2
//====================================================================

        User user1 = new User();
        user1.setFirstName("Arnold");
        user1.setLastName("Schwarzenegger");
        local= new LocalDate(2016,10,8);
        user1.setBirthday(local.toDate());
        user1.setGender(true);
        user1.setUserType(UserType.user);

        UserPrivacy up1 = new UserPrivacy();
        up1.setProfilePrivacy(Privacy.FRIENDS);
        up1.setContactUser(Privacy.FRIENDS);
        up1.setSearchByNameOrEmail(Privacy.FRIENDS_OF_FRIENDS);

        user1.setUserPrivacy(up);


        UserState userState1 = new UserState();

        userState1.setPhoto("https://dl.dropboxusercontent.com/s/bxjmpkrsa6axu6i/Koala.jpg?dl=0");
        userState1.setEmail("asd1@yahoo.com");
        userState1.setPassword(PasswordEncryptorSHA1.encryptPassword("123456"));
        userState1.setUser(user1);

        userRepositoryBean.save(user1);
        userStateRepositoryBean.save(userState1);

        //        USER3
//====================================================================

        User user2 = new User();
        user2.setFirstName("Mickey");
        user2.setLastName("Mouse");

        user2.setBirthday(new Date(8, 2, 2016));
        user2.setGender(true);
        user2.setUserType(UserType.user);

        UserPrivacy up2 = new UserPrivacy();
        up2.setProfilePrivacy(Privacy.FRIENDS);
        up2.setContactUser(Privacy.FRIENDS);
        up2.setSearchByNameOrEmail(Privacy.FRIENDS_OF_FRIENDS);

        user2.setUserPrivacy(up2);

        UserState userState2 = new UserState();

        userState2.setEmail("asd2@yahoo.com");
        userState2.setPassword(PasswordEncryptorSHA1.encryptPassword("123456"));
        userState2.setUser(user2);
        userState2.setPhoto("https://dl.dropboxusercontent.com/s/bxjmpkrsa6axu6i/Koala.jpg?dl=0");

        userRepositoryBean.save(user2);
        userStateRepositoryBean.save(userState2);

//        USER3
//====================================================================

        User user3 = new User();
        user3.setFirstName("Mr.");
        user3.setLastName("Pickless");

        user3.setBirthday(new Date(8, 2, 2016));
        user3.setGender(true);
        user3.setUserType(UserType.user);

        UserPrivacy up3 = new UserPrivacy();
        up3.setProfilePrivacy(Privacy.ONLY_ME);
        up3.setContactUser(Privacy.FRIENDS);
        up3.setSearchByNameOrEmail(Privacy.FRIENDS_OF_FRIENDS);

        user3.setUserPrivacy(up3);

        UserState userState3 = new UserState();

        userState3.setEmail("asd3@yahoo.com");
        userState3.setPassword(PasswordEncryptorSHA1.encryptPassword("123456"));
        userState3.setUser(user3);
        userState3.setPhoto("https://dl.dropboxusercontent.com/s/bxjmpkrsa6axu6i/Koala.jpg?dl=0");

        userRepositoryBean.save(user3);
        userStateRepositoryBean.save(userState3);

//      USER3
//====================================================================

        User user4 = new User();
        user4.setFirstName("Machu");
        user4.setLastName("Picchu");

        user4.setBirthday(new Date(8, 2, 2016));
        user4.setGender(true);
        user4.setUserType(UserType.user);

        UserPrivacy up4 = new UserPrivacy();
        up4.setProfilePrivacy(Privacy.FRIENDS_OF_FRIENDS);
        up4.setContactUser(Privacy.FRIENDS_OF_FRIENDS);
        up4.setSearchByNameOrEmail(Privacy.FRIENDS_OF_FRIENDS);

        user4.setUserPrivacy(up4);

        UserState userState4 = new UserState();

        userState4.setEmail("asd4@yahoo.com");
        userState4.setPassword(PasswordEncryptorSHA1.encryptPassword("123456"));
        userState4.setUser(user4);
        userState4.setPhoto("https://dl.dropboxusercontent.com/s/bxjmpkrsa6axu6i/Koala.jpg?dl=0");

        userRepositoryBean.save(user4);
        userStateRepositoryBean.save(userState4);

        //      PaulMarc
//====================================================================

        User paul = new User();
        paul.setFirstName("Paul");
        paul.setLastName("Marc");

        paul.setBirthday(new Date(07, 20, 1994));
        paul.setGender(true);
        paul.setUserType(UserType.user);

        UserPrivacy upp = new UserPrivacy();
        upp.setProfilePrivacy(Privacy.FRIENDS_OF_FRIENDS);
        upp.setContactUser(Privacy.FRIENDS_OF_FRIENDS);
        upp.setSearchByNameOrEmail(Privacy.FRIENDS_OF_FRIENDS);

        paul.setUserPrivacy(upp);

        UserState paulMarc = new UserState();

        paulMarc.setEmail("paul@yahoo.com");
        paulMarc.setPassword(PasswordEncryptorSHA1.encryptPassword("123456"));
        paulMarc.setUser(paul);
        paulMarc.setPhoto("https://dl.dropboxusercontent.com/s/qusmakc6xlcq3rc/635887103250254550-GTY-505455176.jpg?dl=0");

        userRepositoryBean.save(paul);
        userStateRepositoryBean.save(paulMarc);

        //First post
        Post federer = new Post();
        federer.setText("Federer best shots");
        federer.setVideo("https://dl.dropboxusercontent.com/s/jz8pn5gep7jwvk0/Roger%20Federer%20-%20Best%20Shots%20in%20History%20%5B1080p%5D.mp4?dl=0");
        federer.setOwner(paulMarc);
        federer.setCreationTime(LocalDateTime.now());

        postRepository.save(federer);

        //Second post
        Post tennis = new Post();
        tennis.setText("2009 Tennis highlights");
        tennis.setVideo("https://dl.dropboxusercontent.com/s/e7dgsgfy6pulety/2009%20Tennis%20Match%20of%20the%20Year-%20Highlights%20-%20Waptubes.Com.mp4?dl=0");
        tennis.setOwner(paulMarc);
        tennis.setCreationTime(LocalDateTime.now());

        postRepository.save(tennis);

        //First event
        Event volvoEvent = new Event();
        volvoEvent.setTitle("Volvo Showroom");
        volvoEvent.setText("The brand new xc90");
        String dateDate = "2016-08-13 21:30";
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeDate = LocalDateTime.parse(dateDate, formatterDate);
        volvoEvent.setDateTime(dateTimeDate);
        volvoEvent.setUserAdmin(paulMarc);
        volvoEvent.setCreationTime(LocalDateTime.now());
        eventRepository.save(volvoEvent);

        //Third post
        Post volvo = new Post();
        volvo.setText("Car look");
        volvo.setPhoto("https://dl.dropboxusercontent.com/s/g6loj3yxhbqavkj/volvo.jpg?dl=0");
        volvo.setOwner(paulMarc);
        volvo.setCreationTime(LocalDateTime.now());
        volvo.setEvent(volvoEvent);
        postRepository.save(volvo);

        //UserEvent
        //CREATING FIRST USEREvent
        UserEvent userEventPM = new UserEvent();
        userEventPM.setEvent(volvoEvent);
        userEventPM.setUserState(paulMarc);
        userEventPM.setStatus(true);
        userEventRepository.save(userEventPM);

        //      Iacob
//====================================================================

        User iacob = new User();
        iacob.setFirstName("Iacob");
        iacob.setLastName("Rus");

        iacob.setBirthday(new Date(8, 23, 1994));
        iacob.setGender(true);
        iacob.setUserType(UserType.user);

        UserPrivacy upp1 = new UserPrivacy();
        upp1.setProfilePrivacy(Privacy.FRIENDS_OF_FRIENDS);
        upp1.setContactUser(Privacy.FRIENDS_OF_FRIENDS);
        upp1.setSearchByNameOrEmail(Privacy.FRIENDS_OF_FRIENDS);

        iacob.setUserPrivacy(upp1);

        UserState iacobRus = new UserState();

        iacobRus.setEmail("iacob@yahoo.com");
        iacobRus.setPassword(PasswordEncryptorSHA1.encryptPassword("123456"));
        iacobRus.setUser(iacob);
        iacobRus.setPhoto("https://dl.dropboxusercontent.com/s/zyv90cxojsi0tx3/iacob2.jpg?dl=0");

        userRepositoryBean.save(iacob);
        userStateRepositoryBean.save(iacobRus);



//        POST 1 IACOB
//===================================================================

        Post postIacob1 = new Post();
        postIacob1.setText("Amazing view :)");
        postIacob1.setPhoto("https://dl.dropboxusercontent.com/s/rbna3obstydal54/amazing.jpg?dl=0");
        postIacob1.setOwner(iacobRus);
        postIacob1.setCreationTime(LocalDateTime.now());

        postRepository.save(postIacob1);

//        POST 1 IACOB
//===================================================================

        Post postIacob2 = new Post();
        postIacob2.setText("First day!");
        postIacob2.setPhoto("https://dl.dropboxusercontent.com/s/e1baay1tdfhi822/first.jpg?dl=0");
        postIacob2.setOwner(iacobRus);
        postIacob2.setCreationTime(LocalDateTime.now());

        postRepository.save(postIacob2);

//        EVENT IACOB
//===================================================================

        Event eventIacob = new Event();
        eventIacob.setTitle("Meci Steaua-Dinamo");
        eventIacob.setText("Derbiul etapei.");
        String striacob = "2016-08-13 21:30";
        DateTimeFormatter formatteriacob = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeIacob = LocalDateTime.parse(striacob, formatteriacob);
        eventIacob.setDateTime(dateTimeIacob);
        eventIacob.setUserAdmin(iacobRus);
        eventIacob.setCreationTime(LocalDateTime.now());
        eventRepository.save(eventIacob);


        //      Oct
//====================================================================

        User octa = new User();
        octa.setFirstName("Octavian");
        octa.setLastName("Dogar");

        octa.setBirthday(new Date(04, 28, 1995));
        octa.setGender(true);
        octa.setUserType(UserType.user);

        UserPrivacy octprivacy = new UserPrivacy();
        octprivacy.setProfilePrivacy(Privacy.FRIENDS_OF_FRIENDS);
        octprivacy.setContactUser(Privacy.FRIENDS_OF_FRIENDS);
        octprivacy.setSearchByNameOrEmail(Privacy.FRIENDS_OF_FRIENDS);

        octa.setUserPrivacy(octprivacy);

        UserState octDog = new UserState();

        octDog.setEmail("octa@yahoo.com");
        octDog.setPassword(PasswordEncryptorSHA1.encryptPassword("123456"));
        octDog.setUser(octa);
        octDog.setPhoto("https://dl.dropboxusercontent.com/s/8k6rkvhqvu076kg/profil.jpg?dl=0");

        userRepositoryBean.save(octa);
        userStateRepositoryBean.save(octDog);

        //First post
        Post friends = new Post();
        friends.setText("Cu prietenii");
        friends.setPhoto("https://dl.dropboxusercontent.com/s/dql7aivz4d1fmmv/prieteni.jpg?dl=0");
        friends.setOwner(octDog);
        friends.setCreationTime(LocalDateTime.now());

        postRepository.save(friends);

        //Second post
        Post party = new Post();
        party.setText("In parcare");
        party.setVideo("https://dl.dropboxusercontent.com/s/s9lqpqrc60lgsvq/Dancing%20Crazy%20Russian%20Party.mp4?dl=0");
        party.setOwner(octDog);
        party.setCreationTime(LocalDateTime.now());

        postRepository.save(party);






//===================================================================

        //CREATING FIRST USEREvent
        UserEvent userEventIacob = new UserEvent();
        userEventIacob.setEvent(eventIacob);
        userEventIacob.setUserState(iacobRus);
        userEventIacob.setStatus(true);
        userEventRepository.save(userEventIacob);


//        POST 1 ADMIN USER
//===================================================================

        Post post = new Post();
        post.setText("Today is Friday!");
        post.setPhoto("https://dl.dropboxusercontent.com/s/bxjmpkrsa6axu6i/Koala.jpg?dl=0");
        post.setOwner(userState);
        post.setCreationTime(LocalDateTime.now());

        postRepository.save(post);

//        POST 2 Arnold
//===================================================================

        Post post3 = new Post();
        post3.setText("This is a post created by your friend");
        post3.setPhoto("https://dl.dropboxusercontent.com/s/bxjmpkrsa6axu6i/Koala.jpg?dl=0");
        post3.setOwner(userState1);
        post3.setCreationTime(LocalDateTime.now());

        postRepository.save(post3);


//        POST 3 (with Video) - USER
//===================================================================

        Post post4 = new Post();
        post4.setText("Post with Video!... Hyperspace");
        post4.setVideo("https://dl.dropboxusercontent.com/s/48vxe7tvnn39pvc/Jump%20to%20Hyperspace_mp4.mp4?dl=0");
        post4.setOwner(userState);
        post4.setCreationTime(LocalDateTime.now());

        postRepository.save(post4);


//        ADDING FRIENDSHIP
//===================================================================

        userService.addFriend(profileAssembler.modelToDto(userState), profileAssembler.modelToDto(userState1));
        userService.addFriend(profileAssembler.modelToDto(userState), profileAssembler.modelToDto(userState2));

        userService.addFriend(profileAssembler.modelToDto(userState1), profileAssembler.modelToDto(paulMarc));

        userService.addFriend(profileAssembler.modelToDto(userState2), profileAssembler.modelToDto(userState3));

        userService.addFriend(profileAssembler.modelToDto(userState3), profileAssembler.modelToDto(userState4));

        userService.addFriend(profileAssembler.modelToDto(paulMarc), profileAssembler.modelToDto(iacobRus));

        userService.addFriend(profileAssembler.modelToDto(octDog), profileAssembler.modelToDto(iacobRus));

        userService.addFriend(profileAssembler.modelToDto(octDog), profileAssembler.modelToDto(paulMarc));

//        ADDING FRIEND REQUEST
//===================================================================

        List<UserState> requestsOfU = new LinkedList<>();
        requestsOfU.add(userState3);
        //requestsOfU.add(userState4);
        userState.setRequests(requestsOfU);

        userStateRepositoryBean.merge(userState);
        userStateRepositoryBean.merge(userState3);


//        CREATING EVENT AND ADDING POST: USER1 POSTS TO EVENT 1.
//        HE ISN'T GOING, USER2 IS GOING
//===================================================================

        Event event = new Event();
        event.setTitle("Event1");
        event.setText("bla bla bla");
        String str = "2016-08-11 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        event.setDateTime(dateTime);
        event.setUserAdmin(userState);
        event.setCreationTime(LocalDateTime.now());
        eventRepository.save(event);


//        CREATING SECOND EVENT
//===================================================================

        Event event2 = new Event();
        event2.setTitle("Event2");
        event2.setText("tra la la");
        String str2 = "2016-08-11 12:30";
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter2);
        event2.setDateTime(dateTime2);
        event2.setUserAdmin(userState2);
        event2.setCreationTime(LocalDateTime.now());
        eventRepository.save(event2);




//===================================================================


        //CREATING FIRST USEREvent
        UserEvent userEvent = new UserEvent();
        userEvent.setEvent(event);
        userEvent.setUserState(userState);
        userEvent.setStatus(false);
        userEventRepository.save(userEvent);

        //SETTING POST TO THE FIRST EVENT
        post.setEvent(event);
        postRepository.merge(post);

        //SECOND USEREVENT
        UserEvent userEvent2 = new UserEvent();
        userEvent2.setEvent(event);
        userEvent2.setUserState(userState2);
        userEvent2.setStatus(true);
        userEventRepository.save(userEvent2);

        UserEvent userEvent3 = new UserEvent();
        userEvent3.setEvent(event);
        userEvent3.setUserState(userState3);
        userEvent3.setStatus(false);
        userEventRepository.save(userEvent3);

        // NOTIFICATION
        Notification notification = new Notification();
        notification.setAuthor(userState1);
        notification.setDescription("Some notification");
        notification.setEvent(event);
        notification.setSeen(false);
        notification.setTarget(userState);
        notification.setTime(Calendar.getInstance().getTime());
        notification.setType(NotificationType.POST);
        notificationRepository.save(notification);

    }

}
