package org.hang.live.user.interfaces;


import org.hang.live.user.constants.UserTagsEnum;

/**
 * @Author idea
 * @Date: Created in 15:42 2023/4/16
 * @Description User Tag RPC service
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
