package com.lrm.web;

import com.lrm.exception.NotFoundException;
import com.lrm.po.User;
import com.lrm.service.UserService;
import com.lrm.util.TokenInfo;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户关注
 *
 * @author 山水夜止
 * @version 1.0
 * @date 2021-07-20
 */
@RestController
public class FollowController {
    @Autowired
    private UserService userService;

    /**
     * 取关/关注
     *
     * @param followedUserId 被关注的用户Id
     * @param request        获得当前用户Id
     * @return 取关/关注 成功/失败
     */
    @GetMapping("follow/{followedUserId}")
    public Result followUser(@PathVariable Long followedUserId, HttpServletRequest request) {
        Long followingUserId = TokenInfo.getCustomUserId(request);
        User followingUser = userService.getUser(followingUserId);
        User followedUser = userService.getUser(followedUserId);

        if (followedUser == null) {
            throw new NotFoundException("未查询到该用户");
        }

        //取关
        if (followedUser.getFollowedUsers().contains(followingUser) &&
                followingUser.getFollowingUsers().contains(followedUser)) {
            followedUser.getFollowedUsers().remove(followingUser);
            followedUser.setFollowed(followedUser.getFollowed() - 1);
            followingUser.getFollowingUsers().remove(followedUser);
            followingUser.setFollowing(followedUser.getFollowing() - 1);

            userService.saveUser(followedUser);
            userService.saveUser(followingUser);
            if (!followedUser.getFollowedUsers().contains(followingUser) &&
                    followingUser.getFollowingUsers().contains(followedUser)) {
                return new Result(null, "取关成功");
            } else {
                return new Result(null, "取关失败");
            }
        } else {
            //关注
            followedUser.getFollowedUsers().add(followingUser);
            followedUser.setFollowed(followedUser.getFollowed() + 1);
            followingUser.getFollowingUsers().add(followedUser);
            followingUser.setFollowing(followedUser.getFollowing() + 1);

            userService.saveUser(followedUser);
            userService.saveUser(followingUser);
            if (followedUser.getFollowedUsers().contains(followingUser) &&
                    followingUser.getFollowingUsers().contains(followedUser)) {
                return new Result(null, "关注成功");
            } else {
                return new Result(null, "关注失败");
            }
        }


    }
}
