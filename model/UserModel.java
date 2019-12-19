package com.example.java.algorithm.model;

import android.text.TextUtils;

import com.example.java.algorithm.javabean.Friend;
import com.example.java.algorithm.javabean._User;
import com.example.java.algorithm.model.i.QueryUserListener;
import com.example.java.algorithm.model.i.UpdateCacheListener;
import com.example.java.algorithm.model.i.UpdatePerDoneListener;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class UserModel extends BaseModel {

    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }

    private UserModel() {
    }

    /**
     * TODO 用户管理：2.1、注册
     *
     * @param username
     * @param password
     * @param pwdagain
     * @param listener
     */
    public void register(String username, String password, String pwdagain,String nickname,
                         final LogInListener listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        if (TextUtils.isEmpty(pwdagain)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写确认密码"));
            return;
        }
        if (!password.equals(pwdagain)) {
            listener.done(null, new BmobException(CODE_NULL, "两次输入的密码不一致，请重新输入"));
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            listener.done(null, new BmobException(CODE_NULL, "请输入昵称"));
            return;
        }
        final _User user = new _User();
        user.setUsername(username);
        user.setPassword(password);
        //默认头像
        user.setAvatar("bmob-cdn-18215.b0.upaiyun.com/2018/05/17/6e352fc3407f6978800b870098150824.png");
        user.setNickname(nickname);
        user.signUp(new SaveListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    listener.done(null, null);
                } else {
                    listener.done(null, e);
                }
            }
        });
    }

    /**
     * TODO 用户管理：2.2、登录
     *
     * @param username
     * @param password
     * @param listener
     */
    public void login(String username, String password, final LogInListener listener) {
        if (TextUtils.isEmpty(username)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写用户名"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            listener.done(null, new BmobException(CODE_NULL, "请填写密码"));
            return;
        }
        final _User user = new _User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    listener.done(getCurrentUser(), null);
                } else {
                    listener.done(user, e);
                }
            }
        });
    }

    /**
     * TODO  用户管理：2.3、退出登录
     */
    public void logout() {
        BmobUser.logOut();
    }

    /**
     * TODO 用户管理：2.4、获取当前用户
     *
     * @return
     */
    public _User getCurrentUser() {
        return BmobUser.getCurrentUser(_User.class);
    }


    /**
     * TODO 用户管理：2.5、查询用户
     *
     * @param username
     * @param limit
     * @param listener
     */
    public void queryUsers(String username, final int limit, final FindListener<_User> listener) {
        BmobQuery<_User> query = new BmobQuery<>();
        //去掉当前用户
        try {
            BmobUser user = BmobUser.getCurrentUser();
            query.addWhereNotEqualTo("username", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        listener.done(list, e);
                    } else {
                        listener.done(list, new BmobException(CODE_NULL, "查无此人"));
                    }
                } else {
                    listener.done(list, e);
                }
            }
        });
    }

    /**
     * TODO 用户管理：2.6、查询指定用户信息
     *
     * @param objectId
     * @param listener
     */
    public void queryUserInfo(String objectId, final QueryUserListener listener) {
        BmobQuery<_User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(
                new FindListener<_User>() {
                    @Override
                    public void done(List<_User> list, BmobException e) {
                        if (e == null) {

                            if (list != null && list.size() > 0) {
                                listener.done(list.get(0), null);
                            } else {
                                listener.done(null, new BmobException(000, "查无此人"));
                            }
                        } else {
                            listener.done(null, e);
                        }
                    }
                });
    }

    /**
     * 更新个人信息
     */
    public void updatePerson(UpdatePerListener listener, final UpdatePerDoneListener perDoneListener) {
        listener.updateCondition(getCurrentUser());
        getCurrentUser().save(new SaveListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    perDoneListener.done(user, null);
                } else {
                    perDoneListener.done(user,e);
                }
            }
        });
    }

    /**
     * 更新用户资料和会话资料
     *
     * @param event
     * @param listener
     */
    public void updateUserInfo(MessageEvent event, final UpdateCacheListener listener) {
        final BmobIMConversation conversation = event.getConversation();
        final BmobIMUserInfo info = event.getFromUserInfo();
        final BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String avatar = info.getAvatar();
        String title = conversation.getConversationTitle();
        String icon = conversation.getConversationIcon();
        //SDK内部将新会话的会话标题用objectId表示，因此需要比对用户名和私聊会话标题，后续会根据会话类型进行判断
        if (!username.equals(title) || (avatar != null && !avatar.equals(icon))) {
            UserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
                @Override
                public void done(_User s, BmobException e) {
                    if (e == null) {
                        String name = s.getUsername();
                        String avatar = s.getAvatar();
                        conversation.setConversationIcon(avatar);
                        conversation.setConversationTitle(name);
                        info.setName(name);
                        info.setAvatar(avatar);
                        //TODO 用户管理：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().updateUserInfo(info);
                        //TODO 会话：4.7、更新会话资料-如果消息是暂态消息，则不更新会话资料
                        if (!msg.isTransient()) {
                            BmobIM.getInstance().updateConversation(conversation);
                        }
                    } else {
                        Logger.e(e);
                    }
                    listener.done(null);
                }
            });
        } else {
            listener.done(null);
        }
    }


//    //TODO 好友管理：9.12、添加好友
    public void agreeAddFriend(_User friend, SaveListener<String> listener) {
        Friend f = new Friend();
        _User user = BmobUser.getCurrentUser(_User.class);
        f.setUser(user);
        f.setFriendUser(friend);
        f.save(listener);
    }

    /**
     * 查询好友
     *
     * @param listener
     */
    //TODO 好友管理：9.2、查询好友
    public void queryFriends(final FindListener<Friend> listener) {
//        BmobQuery<Friend> query = new BmobQuery<>();
//        User user = BmobUser.getCurrentUser(User.class);
//        query.addWhereEqualTo("user", user);
//        query.include("friendUser");
//        query.order("-updatedAt");
//        query.findObjects(new FindListener<Friend>() {
//            @Override
//            public void done(List<Friend> list, BmobException e) {
//                if (e == null) {
//                    if (list != null && list.size() > 0) {
//                        listener.done(list, e);
//                    } else {
//                        listener.done(list, new BmobException(0, "暂无联系人"));
//                    }
//                } else {
//                    listener.done(list, e);
//                }
//            }
//        });
    }

    /**
     * 删除好友
     *
     * @param f
     * @param listener
     */
    //TODO 好友管理：9.3、删除好友
    public void deleteFriend(Friend f, UpdateListener listener) {
        Friend friend = new Friend();
        friend.delete(f.getObjectId(), listener);
    }

    public void updateUserInfo(UpdateListener updateListener, UpdatePerListener queryPostListener) {
        _User user = getCurrentUser();
        if (queryPostListener != null) {
            queryPostListener.updateCondition(user);
        }
        user.update(user.getObjectId(), updateListener);
    }
}
