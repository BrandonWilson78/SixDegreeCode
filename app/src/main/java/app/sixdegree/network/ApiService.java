package app.sixdegree.network;

import java.util.HashMap;

import app.sixdegree.model.login.LoginResponseData;
import app.sixdegree.network.responses.BaseRes;
import app.sixdegree.network.responses.GetNotificationBadgeCount;
import app.sixdegree.network.responses.addeditresponses.AddEditResponse;
import app.sixdegree.network.responses.addtripres.AddTripRes;
import app.sixdegree.network.responses.bannersres.BannerRes;
import app.sixdegree.network.responses.content_res.ContentRes;
import app.sixdegree.network.responses.edittripres.EditTripRes;
import app.sixdegree.network.responses.exlporeres.ExlporeListRes;
import app.sixdegree.network.responses.fetchcommentres.FetchCommentC;
import app.sixdegree.network.responses.fetchcomments.FetchCommentsRes;
import app.sixdegree.network.responses.fetchcontacts.FetchContactsRes;
import app.sixdegree.network.responses.fetchsharingoptionsres.FetchSharingOptionsRes;
import app.sixdegree.network.responses.followerres.FollowerRes;
import app.sixdegree.network.responses.followingres.FollowingRes;
import app.sixdegree.network.responses.getSingletripDetails.SingleTripDetailsRes;
import app.sixdegree.network.responses.getallchats.GetAllChatsRes;

import app.sixdegree.network.responses.getallsavedpitstops.GetAllSavedPitstops;
import app.sixdegree.network.responses.getblockedusersres.GetBlockedUsersRes;
import app.sixdegree.network.responses.getfollowerrequests.GetFollowerRequestsRes;
import app.sixdegree.network.responses.getfriendslisting.GetFriendsListing;
import app.sixdegree.network.responses.getinboxres.GetInboxRes;
import app.sixdegree.network.responses.getlatesttrip.GetLatestTripRes;
import app.sixdegree.network.responses.getmutualfriends.GetMutualFriendsRes;
import app.sixdegree.network.responses.getpendingrequests.GetPendingRequests;
import app.sixdegree.network.responses.getprevioustrips.GetPreviousTripsRes;
import app.sixdegree.network.responses.getsingleTrailDetails.GetSingleTrailDetails;
import app.sixdegree.network.responses.gettaggedtrips.GetTaggedTripsRes;
import app.sixdegree.network.responses.gettagnotifications.GetTagNotificationRes;
import app.sixdegree.network.responses.gettripcategories.GetTripCategories;
import app.sixdegree.network.responses.gettripdetailsnewres.GetTripDetailsResNew;
import app.sixdegree.network.responses.profileupdatees.ProfileUpdateres;
import app.sixdegree.network.responses.searchfriends.SearchFriendsRes;
import app.sixdegree.network.responses.sendmessage.SendMessageRes;
import app.sixdegree.network.responses.settings_mod.profile.UserProfile;
import app.sixdegree.network.responses.showlikeslist.ViewLikeList;
import app.sixdegree.network.responses.signupres.SignupResData;
import app.sixdegree.network.responses.sixdegreesearch.SixDegreeSearchRes;
import app.sixdegree.network.responses.tripdetailsres.SingleTripRes;
import app.sixdegree.network.responses.tripsinterest.InterestTripRes;
import app.sixdegree.network.responses.viewfriends.ViewFriends;
import app.sixdegree.network.responses.viewprofilefriends.ViewFriendsProfile;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface ApiService {
    String PATH = "6Degree/api/";
    String LOGIN_API = PATH + "login";
    String BANNERS_API = PATH + "banners";
    String VERIFY_EMAIL = PATH + "verify-email";
    String FORGOT_PASS = PATH + "forgot-password";
    String SET_PROFILE_TYPE = PATH + "set-profile-type";
    String LOGOUT_USER = PATH + "logout";
    String OUR_STORY = PATH + "our-story";
    String LEGAL = PATH + "legal";
    String CONTACT_US = PATH + "contact-us";
    String DETAILS = PATH + "details";
    String UPDATE_PROFILE = PATH + "update-profile";
    String UPDATE_ACCOUNT = PATH + "update-user-account";
    String UPDATE_TEMP = PATH + "update-temp";
    String UPDATE_UNIT = PATH + "update-units";
    String UPDATE_MAP = PATH + "update-map";
    String REGISTER_USER = PATH + "register";
    String GET_USER_FOLLOWING = PATH + "get-followings";
    String GET_USER_FOLLOWERS = PATH + "get-followers";
    String UNFOLLOW_USER = PATH + "unfollow-user";
    String TRAIL_CATEGORIES = PATH + "trail-interests";
    String FOLLOW_USER = PATH + "follow-user";
    String REMOVE_FOLLOWER = PATH + "remove-follower";
    String GET_PENDING_REQUESTS = PATH + "get-unapproved-requests";
    String EXPLORE_RES = PATH + "get-explore";
    String SAVE_USER_INTEREST = PATH + "save-user-intrests";
    String GET_TRIPS_BY_INTEREST = PATH + "get-trips-by-interest";
    String SINGLE_TRIP_DETAILS = PATH + "single-trip-detail";
    String ADD_EDIT_TRIP = PATH + "add-trip-to-trail";
    String GET_TRIP_CATEGORIES = PATH + "interests";
    String SINGLE_TRAIL_DETAILS = PATH + "single-trail-detail";
    String GET_TRAIL_COMMENTS = PATH + "get-comments";
    String AAD_TRAIL_COMMENTS = PATH + "add-comment-trail";
    String LIKE_UNLIKE_TRAIL = PATH + "like-unlike-trail";
    String EDIT_TRAIL = PATH + "edit-trail";

    String VIEW_FRIENDS = PATH + "view-friends";
    String SEARCH_USERS_BY_LOCATION = PATH + "search-users-by-location";
    String SHOW_LIKES = PATH + "show-likes";
    String GET_FOLLOWER_REQUESTS = PATH + "get-follower-requests";
    String SEARCHSIXDEGREE_WITHLOCATION = PATH + "search-six-degree-match";
    String GET_INBOX_MESSAGE = PATH + "get-inbox";
    String GET_ALL_CHAT_MESSAGES = PATH + "get-all-chat";
    String SEND_MESSAGE = PATH + "send-message";
    String RECEIVE_MESSAGE = PATH + "recent-messages";
    String ACCEPT_REQUEST = PATH + "respond-request";
    String REJECT_REQUEST = PATH + "respond-request";
    String BLOCK_USER = PATH + "block-user";
    String GET_BLOCK_USER = PATH + "get-block-users";
    String UNBLOCK_USERS = PATH + "unblock-user";
    String SOCIAL_LOGIN = PATH + "social";
    String GET_MUTUAL_FRIENDS = PATH + "get-mutual-friends";
    String UPDATE_USERS_LOCATION = PATH + "update-current-location";
    String FRIENDS_VIEW_PROFILE = PATH + "view-other-user-profile";
    String ADD_TRIP = PATH + "add-trip";
    String EDIT_TRIP = PATH + "edit-trip";
    String SINGLE_TRIP_ = PATH + "single-trip-detail";
    String FETCH_SHARING_OPTIONS = PATH + "fetch-sharing-options";
    String RESEND_EMAIL = PATH + "resend-email";
    String GET_PREVIOUS_TRIPS = PATH + "get-previous-trips";
    String GET_LATEST_TRIP = PATH + "get-latest-trip";
    String GET_TAG_NOTIFICATIONS = PATH + "get-notifications";
    String GET_TAGGED_TRIPS = PATH + "get-tagged-trips";
    String CONTACTS = PATH + "contacts";
    String SEARCH_USERS_BY_NAME = PATH + "search-users-by-name";
    String SEND_FRIEND_REQUEST = PATH + "new-send-request";
    String GET_FRIEND_REQUESTS = PATH + "get-friend-requests";
    String ACCEPT_FRIEND_REQUESTS = PATH + "accept-friend-request";
    String REJECT_FRIEND_REQUEST = PATH + "reject-friend-request";
    String REMOVE_FRIEND = PATH + "remove-friend";
    String GET_FRIEND_LISTING = PATH + "get-friend-listing";
    String READ_NOTIFICATION = PATH + "mark-read-notification";
    String GET_NOTIFICATIONBADGE_COUNT = PATH + "get-unread-notification-count";
    String DELETE_TRIP = PATH + "delete-trip";
    String DELETE_PITSTOP = PATH + "delete-trip-trail";
    String GETBOOKMARKTRIPS = PATH + "getbookmarks";
    String SAVE_PITSTOP = PATH + "bookmarkPitstop";
    String REMOVE_BOOKMARK = PATH + "removeBookmark";


    @POST(LOGIN_API)
    @FormUrlEncoded
    Observable<LoginResponseData> loginViaEmail(@Field("email") String email, @Field("password") String password, @Field("device_token") String devicetoken);

    @GET(BANNERS_API)
    Observable<BannerRes> getBanners();

    @POST(VERIFY_EMAIL)
    @FormUrlEncoded
    Observable<BaseRes> verifyEmail(@Field("email") String userEmail, @Field("email_verification_code") String code, @Header("Authorization") String token);

    @POST(FORGOT_PASS)
    @FormUrlEncoded
    Observable<BaseRes> forgotPassword(@Field("email") String email);

    @POST(SET_PROFILE_TYPE)
    @FormUrlEncoded
    Observable<BaseRes> setProfileType(@Header("Authorization") String token, @Field("account_type") String account_type, @Field("user_id") String user_id);

    @POST(LOGOUT_USER)
    Observable<BaseRes> logOutUser(@Header("Authorization") String token);

    @GET
    Observable<ContentRes> getPageContent(@Header("Authorization") String token, @Url String url);

    @POST(DETAILS)
    Observable<UserProfile> getProfileDetails(@Header("Authorization") String token);

    @POST(UPDATE_ACCOUNT)
    @FormUrlEncoded
    Observable<BaseRes> updateUserProfileWithoutImages(@Header("Authorization") String token, @FieldMap HashMap<String, String> app);

    @Multipart
    @POST(UPDATE_PROFILE)
    Observable<ProfileUpdateres> updateUserProfile(@Header("Authorization") String token,
                                                   @Part("name") RequestBody name,
                                                   @Part("surname") RequestBody surname,
                                                   @Part("mobile") RequestBody mobile,
                                                   @Part("bio") RequestBody bio,
                                                   @Part("home") RequestBody home,
                                                   @Part("country") RequestBody country,
                                                   @Part("lat") RequestBody lat,
                                                   @Part("long") RequestBody lng,
                                                   @Part MultipartBody.Part profileImage,
                                                   @Part MultipartBody.Part coverImage);//image

    @POST(UPDATE_TEMP)
    @FormUrlEncoded
    Observable<BaseRes> updateTemp(@Header("Authorization") String token, @Field("temprature") String temprature);

    @POST(UPDATE_UNIT)
    @FormUrlEncoded
    Observable<BaseRes> updateUnits(@Header("Authorization") String token, @Field("radius_unit") String radius_unit);

    @POST(UPDATE_MAP)
    @FormUrlEncoded
    Observable<BaseRes> updateMap(@Header("Authorization") String token, @Field("maptype") String radius_unit);

    @POST(REGISTER_USER)
    @FormUrlEncoded
    Observable<SignupResData> registerUser(@FieldMap HashMap<String, String> app);

    @GET(GET_USER_FOLLOWERS)
    Observable<FollowerRes> getFollowers(@Header("Authorization") String token, @Query("name") String name, @Query("user_id") String user_id);

    @GET(GET_USER_FOLLOWING)
    Observable<FollowingRes> getFollowing(@Header("Authorization") String token, @Query("name") String name, @Query("user_id") String user_id);

    @POST(UNFOLLOW_USER)
    @FormUrlEncoded
    Observable<BaseRes> unFollowUser(@Header("Authorization") String token, @Field("following_user_id") String following_user_id);

    @POST(REMOVE_FOLLOWER)
    @FormUrlEncoded
    Observable<BaseRes> removeFollower(@Header("Authorization") String token, @Field("follower_user_id") String following_user_id);

    @Multipart
    @POST(REGISTER_USER)
    Observable<LoginResponseData> registerUser(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("password") RequestBody password,
            @Part("home") RequestBody home,
            @Part("country") RequestBody country,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("device_token") RequestBody device_token,
            @Part MultipartBody.Part profile);//image

    @GET(EXPLORE_RES)
    Observable<ExlporeListRes> getUserExplore(@Header("Authorization") String token);

    @POST(SAVE_USER_INTEREST)
    @FormUrlEncoded
    Observable<BaseRes> saveUserInterest(@Header("Authorization") String token, @Field("category_ids") String category_ids);

    @GET(GET_TRIPS_BY_INTEREST)
    Observable<InterestTripRes> getTripsByInterest(@Header("Authorization") String token, @Query("latitude") String latitude
            , @Query("longitude") String longitude, @Query("start_date") String start_date, @Query("end_date") String end_da
            , @Query("orderby") String orderby, @Query("popular") String popular, @Query("location") String location,
                                                   @Query("user_id") String user_id, @Query("trip_name") String tripname, @Query("trip_category")
                                                           String trip_category, @Query("search_trips_of") String search_trips_of/*,@Query(  "page" ) String page*/);

    @POST(SINGLE_TRIP_DETAILS)
    @FormUrlEncoded
    Observable<GetTripDetailsResNew> getTripDetails(@Header("Authorization") String token, @Field("trip_id") String trip_id);

    // add edit trail
    @Multipart
    @POST(ADD_EDIT_TRIP)
    Observable<AddEditResponse> addEditTrail(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("summary") RequestBody summary,
            @Part("start_date") RequestBody start_date,
            @Part("end_date") RequestBody end_date,
            @Part("who_can_see") RequestBody who_can_see,
            @Part("category") RequestBody category,
            @Part("map_style") RequestBody map_style,
            @Part("status") RequestBody status,
            @Part("trip_id") RequestBody trip_id,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("location") RequestBody location,
            @Part("country") RequestBody country,
            @Part MultipartBody.Part trail_pictures);//image

    //GET TRIP CATEGORIES
    @GET(GET_TRIP_CATEGORIES)
    Observable<GetTripCategories> getTripCategories(@Header("Authorization") String token);

    //GET TRIP CATEGORIES
    @GET(TRAIL_CATEGORIES)
    Observable<GetTripCategories> getTrailCategories(@Header("Authorization") String token);

    //GET SAVED PITSTOP
    @GET(GETBOOKMARKTRIPS)
    Observable<GetAllSavedPitstops> getAllBookmarksPitstop(@Header("Authorization") String token);

    //get single trail details
    @POST(SINGLE_TRAIL_DETAILS)
    @FormUrlEncoded
    Observable<GetSingleTrailDetails> getSingleTrailDetails(@Header("Authorization") String token, @Field("trail_id") String trip_id);

    //DELETE TRIP
    @POST(DELETE_TRIP)
    @FormUrlEncoded
    Observable<BaseRes> deleteTrip(@Header("Authorization") String token, @Field("trip_id") String trip_id);

    //Bookmark TRIP
    @POST(SAVE_PITSTOP)
    @FormUrlEncoded
    Observable<BaseRes> savePitstop(@Header("Authorization") String token, @Field("pitstop_id") String trip_id);


    @POST(REMOVE_BOOKMARK)
    @FormUrlEncoded
    Observable<BaseRes> removePitstop(@Header("Authorization") String token, @Field("pitstop_id") String trip_id);

    //DELETE Pitstop
    @POST(DELETE_PITSTOP)
    @FormUrlEncoded
    Observable<BaseRes> deletePitstop(@Header("Authorization") String token, @Field("trail_id") String trail_id);


    //EDIT TRAIL details
    @Multipart
    @POST(EDIT_TRAIL)
    Observable<AddEditResponse> editTrail(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("summary") RequestBody summary,
            @Part("start_date") RequestBody start_date,
            @Part("end_date") RequestBody end_date,
            @Part("who_can_see") RequestBody who_can_see,
            @Part("category") RequestBody category,
            @Part("map_style") RequestBody map_style,
            @Part("status") RequestBody status,
            @Part("trip_id") RequestBody trip_id,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("location") RequestBody location,
            @Part("country") RequestBody country,
            @Part("trail_id") RequestBody trail_id,
            @Part MultipartBody.Part trail_pictures);//image

    //like unlike trail
    @POST(LIKE_UNLIKE_TRAIL)
    @FormUrlEncoded
    Observable<BaseRes> likeUnlikeTrail(@Header("Authorization") String token, @FieldMap HashMap<String, String> app);

    //fetch trail Comments
    @POST(GET_TRAIL_COMMENTS)
    @FormUrlEncoded
    Observable<FetchCommentsRes> fetchComments(@Header("Authorization") String token, @Field("trail_id") String trail_id);


    //add trail Comments
    @POST(AAD_TRAIL_COMMENTS)
    @FormUrlEncoded
    Observable<FetchCommentC> addCommentToTrail(@Header("Authorization") String token, @Field("trail_id") String trail_id, @Field("comment") String comment);

    //view friends //bydefault- all
    @POST(VIEW_FRIENDS)
    @FormUrlEncoded
    Observable<ViewFriends> viewFriends(@Header("Authorization") String token, @Field("type") String type);


    // search users by location
    @POST(SEARCH_USERS_BY_LOCATION)
    @FormUrlEncoded
    Observable<SearchFriendsRes> searchFriendByLocation(@Header("Authorization") String token, @FieldMap HashMap<String, String> hashMap);

    // search users by location
    @GET(SHOW_LIKES)
    Observable<ViewLikeList> showLikesRes(@Header("Authorization") String token, @QueryMap HashMap<String, String> hashMap);


    // GET follower requests
    @GET(GET_FOLLOWER_REQUESTS)
    Observable<GetFollowerRequestsRes> getFollowerRequests(@Header("Authorization") String token);


    // GET pending requests
    @GET(GET_PENDING_REQUESTS)
    Observable<GetPendingRequests> getPendingRequests(@Header("Authorization") String token);

    // GET getNotificationBadgeCount
    @GET(GET_NOTIFICATIONBADGE_COUNT)
    Observable<GetNotificationBadgeCount> getNotificationBadgeCount(@Header("Authorization") String token);

    // GET getNotificationBadgeCount
    @GET(READ_NOTIFICATION)
    Observable<BaseRes> readNotification(@Header("Authorization") String token);

    // GET follower requests
    @GET(SEARCHSIXDEGREE_WITHLOCATION)
    Observable<SixDegreeSearchRes> searchSixDegree(@Header("Authorization") String token, @QueryMap HashMap<String, String> hashMap);

    // GET inbox messages
    @POST(GET_INBOX_MESSAGE)
    Observable<GetInboxRes> getInboxMessages(@Header("Authorization") String token);

    // get all chat messages
    @GET(GET_ALL_CHAT_MESSAGES)
    Observable<GetAllChatsRes> getAllChatMessages(@Header("Authorization") String token, @QueryMap HashMap<String, String> hashMap);


    //send message
    @POST(SEND_MESSAGE)
    @FormUrlEncoded
    Observable<SendMessageRes> sendMessage(@Header("Authorization") String token, @FieldMap HashMap<String, String> app);

    //send message
    @GET(RECEIVE_MESSAGE)
    Observable<GetAllChatsRes> receiveMsg(@Header("Authorization") String token, @QueryMap HashMap<String, String> app);


    //accept message
    @POST(ACCEPT_REQUEST)
    @FormUrlEncoded
    Observable<BaseRes> acceptRequest(@Header("Authorization") String token, @FieldMap HashMap<String, String> app);


    //accept message
    @POST(REJECT_REQUEST)
    @FormUrlEncoded
    Observable<BaseRes> rejectRequest(@Header("Authorization") String token, @FieldMap HashMap<String, String> app);

    @POST(FOLLOW_USER)
    @FormUrlEncoded
    Observable<BaseRes> followuser(@Header("Authorization") String token, @Field("to_user_id") String following_user_id);


    //block user
    @POST(BLOCK_USER)
    @FormUrlEncoded
    Observable<BaseRes> blockuser(@Header("Authorization") String token, @Field("block_user_id") String following_user_id);

    //get blocked users
    @GET(GET_BLOCK_USER)
    Observable<GetBlockedUsersRes> getBlockedUsers(@Header("Authorization") String token);

    //UNblock user
    @POST(UNBLOCK_USERS)
    @FormUrlEncoded
    Observable<BaseRes> unblockUser(@Header("Authorization") String token, @Field("blocked_user_id") String following_user_id);

    //social login
    @POST(SOCIAL_LOGIN)
    @FormUrlEncoded
    Observable<LoginResponseData> socialLogin(@FieldMap HashMap<String, String> app);


    //accept message
    @POST(UPDATE_USERS_LOCATION)
    @FormUrlEncoded
    Observable<BaseRes> updateLocation(@Header("Authorization") String token, @FieldMap HashMap<String, String> app);


    @POST(FRIENDS_VIEW_PROFILE)
    @FormUrlEncoded
    Observable<ViewFriendsProfile> getFriendProfile(@Header("Authorization") String token, @Field("user_id") String following_user_id);

    @GET(GET_MUTUAL_FRIENDS)
    Observable<GetMutualFriendsRes> getMutualFriends(@Header("Authorization") String token, @Query("user_id") String following_user_id);

    //add trip
    @Multipart
    @POST(ADD_TRIP)
    Observable<AddTripRes> addTrip(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("summary") RequestBody summary,
            @Part("start_date") RequestBody start_date,
            @Part("end_date") RequestBody end_date,
            @Part("who_can_see") RequestBody who_can_see,
            @Part("category") RequestBody category,
            @Part("map_style") RequestBody map_style,
            @Part("status") RequestBody status,
            @Part("tag_ids") RequestBody tag_ids,
            @Part MultipartBody.Part trip_picture);//image


    //get single trip details
    @Multipart
    @POST(EDIT_TRIP)
    Observable<EditTripRes> editTrip(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("summary") RequestBody summary,
            @Part("start_date") RequestBody start_date,
            @Part("end_date") RequestBody end_date,
            @Part("who_can_see") RequestBody who_can_see,
            @Part("category") RequestBody category,
            @Part("map_style") RequestBody map_style,
            @Part("status") RequestBody status,
            @Part("trip_id") RequestBody trip_id,
            @Part("tag_ids") RequestBody tag_ids,
            @Part MultipartBody.Part trail_pictures);//image


    @POST(SINGLE_TRIP_)
    @FormUrlEncoded
    Observable<SingleTripDetailsRes> getSingleTripDetails(@Header("Authorization") String token, @Field("trip_id") String trip_id);

    @POST(FETCH_SHARING_OPTIONS)
    Observable<FetchSharingOptionsRes> fetchSharingOptions(@Header("Authorization") String token);


    @GET(RESEND_EMAIL)
    Observable<BaseRes> resendCode(@Header("Authorization") String token);


    //GET_PREVIOUS_TRIPS
    @GET(GET_PREVIOUS_TRIPS)
    Observable<GetPreviousTripsRes> getPreviousTrips(@Header("Authorization") String token, @Query("user_id") String id);

    //GET_LATEST_TRIP
    @GET(GET_LATEST_TRIP)
    Observable<GetLatestTripRes> getLatestTrip(@Header("Authorization") String token);


    //GET_TAG_NOTIFICATIONS
    @GET(GET_TAG_NOTIFICATIONS)
    Observable<GetTagNotificationRes> getTagNoti(@Header("Authorization") String token);

    @POST(GET_TAGGED_TRIPS)
    @FormUrlEncoded
    Observable<GetTaggedTripsRes> getTaggedTripsRes(@Header("Authorization") String token, @Field("user_id") String user_id);


    @POST(CONTACTS)
    @FormUrlEncoded
    Observable<FetchContactsRes> sendContacts(@Header("Authorization") String token, @Field("contacts") String user_id);


    @POST(SEARCH_USERS_BY_NAME)
    @FormUrlEncoded
    Observable<SixDegreeSearchRes> searchUsersByName(@Header("Authorization") String token, @FieldMap HashMap<String, String> app);


    @POST(SEND_FRIEND_REQUEST)
    @FormUrlEncoded
    Observable<BaseRes> sendFriendRequest(@Header("Authorization") String token, @Field("to_user_id") String following_user_id);

    // GET friends requests
    @GET(GET_FRIEND_REQUESTS)
    Observable<GetFollowerRequestsRes> getFriendRequests(@Header("Authorization") String token);

    //accept friend requests
    @POST(ACCEPT_FRIEND_REQUESTS)
    @FormUrlEncoded
    Observable<BaseRes> acceptFriendRequests(@Header("Authorization") String token, @FieldMap HashMap<String, String> app);

    //reject friend requests
    @POST(REJECT_FRIEND_REQUEST)
    @FormUrlEncoded
    Observable<BaseRes> rejectFriendRequests(@Header("Authorization") String token, @FieldMap HashMap<String, String> app);

    //get friends //bydefault- all
    @POST(GET_FRIEND_LISTING)
    @FormUrlEncoded
    Observable<GetFriendsListing> getFriendsListing(@Header("Authorization") String token, @Field("type") String type);

    @POST(REMOVE_FRIEND)
    @FormUrlEncoded
    Observable<BaseRes> removeFriend(@Header("Authorization") String token, @Field("user_id") String following_user_id);


}