package com.lbz.gmall.user;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallUserApplicationTests {

    @Test
    public void contextLoads() {

        String jsonString = "{\"id\":5695659994,\"idstr\":\"5695659994\",\"class\":1,\"screen_name\":\"用户5695659994\",\"name\":\"用户5695659994\",\"province\":\"12\",\"city\":\"1000\",\"location\":\"天津\",\"description\":\"\",\"url\":\"\",\"profile_image_url\":\"https://tvax1.sinaimg.cn/default/images/default_avatar_male_50.gif?KID=imgbed,tva&Expires=1568654058&ssig=xtPEEFXZbv\",\"profile_url\":\"u/5695659994\",\"domain\":\"\",\"weihao\":\"\",\"gender\":\"m\",\"followers_count\":1,\"friends_count\":2,\"pagefriends_count\":0,\"statuses_count\":2,\"video_status_count\":0,\"favourites_count\":0,\"created_at\":\"Mon Sep 07 08:29:58 +0800 2015\",\"following\":false,\"allow_all_act_msg\":false,\"geo_enabled\":true,\"verified\":false,\"verified_type\":-1,\"remark\":\"\",\"insecurity\":{\"sexual_content\":false},\"status\":{\"created_at\":\"Mon Jul 16 22:39:39 +0800 2018\",\"id\":4262539103698029,\"idstr\":\"4262539103698029\",\"mid\":\"4262539103698029\",\"can_edit\":false,\"show_additional_indication\":0,\"text\":\"旭旭宝宝天下第一！\",\"source_allowclick\":1,\"source_type\":1,\"source\":\"<a href=\\\"http://app.weibo.com/t/feed/6vDmqx\\\" rel=\\\"nofollow\\\">魅蓝 Note3</a>\",\"favorited\":false,\"truncated\":false,\"in_reply_to_status_id\":\"\",\"in_reply_to_user_id\":\"\",\"in_reply_to_screen_name\":\"\",\"pic_urls\":[],\"geo\":null,\"is_paid\":false,\"mblog_vip_type\":0,\"annotations\":[{\"client_mblogid\":\"192b7638-2036-408a-8492-1d12024cf402\"},{\"mapi_request\":true}],\"reposts_count\":0,\"comments_count\":0,\"attitudes_count\":0,\"pending_approval_count\":0,\"isLongText\":false,\"reward_exhibition_type\":0,\"hide_flag\":0,\"mlevel\":0,\"visible\":{\"type\":0,\"list_id\":0},\"biz_feature\":0,\"hasActionTypeCard\":0,\"darwin_tags\":[],\"hot_weibo_tags\":[],\"text_tag_tips\":[],\"mblogtype\":0,\"rid\":\"0\",\"userType\":0,\"more_info_type\":0,\"positive_recom_flag\":0,\"content_auth\":0,\"gif_ids\":\"\",\"is_show_bulletin\":2,\"comment_manage_info\":{\"comment_permission_type\":-1,\"approval_comment_type\":0},\"pic_num\":0},\"ptype\":0,\"allow_all_comment\":true,\"avatar_large\":\"https://tvax1.sinaimg.cn/default/images/default_avatar_male_180.gif?KID=imgbed,tva&Expires=1568654058&ssig=MMDp%2FqmcR%2B\",\"avatar_hd\":\"https://tvax1.sinaimg.cn/default/images/default_avatar_male_180.gif?KID=imgbed,tva&Expires=1568654058&ssig=MMDp%2FqmcR%2B\",\"verified_reason\":\"\",\"verified_trade\":\"\",\"verified_reason_url\":\"\",\"verified_source\":\"\",\"verified_source_url\":\"\",\"follow_me\":false,\"like\":false,\"like_me\":false,\"online_status\":0,\"bi_followers_count\":0,\"lang\":\"zh-cn\",\"star\":0,\"mbtype\":0,\"mbrank\":0,\"block_word\":0,\"block_app\":0,\"credit_score\":80,\"user_ability\":0,\"urank\":4,\"story_read_state\":-1,\"vclub_member\":0,\"is_teenager\":0,\"is_guardian\":0,\"is_teenager_list\":0}";
        Map<String,Object> map = JSON.parseObject(jsonString, Map.class);
        System.out.println((String) map.get("name"));
        System.out.println("....");

    }

}
