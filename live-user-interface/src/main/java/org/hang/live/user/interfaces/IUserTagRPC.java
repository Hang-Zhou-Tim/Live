package org.hang.live.user.interfaces;


import org.hang.live.user.constants.UserTagsEnum;

/**
 * @Author hang
 * @Date: Created in 10:13 2024/8/10
 * @Description User tag rpc
 */
public interface IUserTagRPC {

   /**
    * Set the tag
    *
    * @param userId
    * @param userTagsEnum
    * @return
    */
   boolean setTag(Long userId, UserTagsEnum userTagsEnum);

   /**
    * Cancel the tag
    *
    * @param userId
    * @param userTagsEnum
    * @return
    */
   boolean cancelTag(Long userId,UserTagsEnum userTagsEnum);

   /**
    * Is the user's tag contained the tag
    *
    * @param userId
    * @param userTagsEnum
    * @return
    */
   boolean containTag(Long userId,UserTagsEnum userTagsEnum);
}
