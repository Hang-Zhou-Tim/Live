package org.hang.live.id.generator.interfaces;

public interface IdGenerateRPC {
    //Get Sequence ID
    Long getSeqId(Integer id);
    //Get Unordered ID
    Long getUnSeqId(Integer id);

}
