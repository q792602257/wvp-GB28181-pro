package com.genersoft.iot.vmp.storager.dao;

import com.genersoft.iot.vmp.media.zlm.dto.StreamProxy;
import com.genersoft.iot.vmp.service.bean.GPSMsgInfo;
import com.genersoft.iot.vmp.vmanager.bean.ResourceBaseInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StreamProxyMapper {

    @Insert("INSERT INTO wvp_stream_proxy (type, name, app, stream,media_server_id, url, dst_url, " +
            "timeout_ms, ffmpeg_cmd_key, rtp_type, enable_audio, enable_mp4, pulling, stream_key, " +
            "enable_remove_none_reader, enable_disable_none_reader, create_time, longitude, latitude, " +
            "common_gb_channel_id, gb_id) VALUES " +
            "(#{type}, #{name}, #{app}, #{stream}, #{mediaServerId}, #{url}, #{dstUrl}, " +
            "#{timeoutMs}, #{ffmpegCmdKey}, #{rtpType}, #{enableAudio}, #{enableMp4}, #{pulling}, #{streamKey}, " +
            "#{enableRemoveNoneReader}, #{enableDisableNoneReader}, #{createTime} , #{longitude} , #{latitude}, " +
            "#{commonGbChannelId}, #{gbId})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int add(StreamProxy streamProxy);

    @Update("UPDATE wvp_stream_proxy " +
            "SET type=#{type}, " +
            "name=#{name}," +
            "app=#{app}," +
            "stream=#{stream}," +
            "url=#{url}, " +
            "media_server_id=#{mediaServerId}, " +
            "dst_url=#{dstUrl}, " +
            "timeout_ms=#{timeoutMs}, " +
            "ffmpeg_cmd_key=#{ffmpegCmdKey}, " +
            "rtp_type=#{rtpType}, " +
            "enable_audio=#{enableAudio}, " +
            "pulling=#{pulling}, " +
            "stream_key=#{streamKey}, " +
            "enable_remove_none_reader=#{enableRemoveNoneReader}, " +
            "enable_disable_none_reader=#{enableDisableNoneReader}, " +
            "enable_mp4=#{enableMp4} " +
            "WHERE app=#{app} AND stream=#{stream}")
    int update(StreamProxy streamProxyDto);

    @Delete("DELETE FROM wvp_stream_proxy WHERE app=#{app} AND stream=#{stream}")
    int del(String app, String stream);

    @Select(" <script>" +
            "SELECT * FROM wvp_stream_proxy where 1 = 1 " +
            " <if test='query != null'> AND (app LIKE '%${query}%' OR stream LIKE '%${query}%' OR name LIKE '%${query}%')</if> " +
            " <if test='mediaServerId != null'> AND media_server_id=#{mediaServerId}</if> " +
            " <if test='pulling == true' > AND pulling=true</if>" +
            " <if test='pulling == false' > AND pulling=false</if>" +
            "order by create_time desc"+
            " </script>" )
    List<StreamProxy> selectAll(@Param("query") String query,
                                @Param("pulling") Boolean pulling,
                                @Param("mediaServerId") String mediaServerId);


    @Select("SELECT st.* from wvp_stream_proxy st WHERE st.app=#{app} AND st.stream=#{stream}")
    StreamProxy selectOne(@Param("app") String app, @Param("stream") String stream);

    @Update("UPDATE wvp_stream_proxy " +
            "SET pulling=#{pulling} " +
            "WHERE media_server_id=#{mediaServerId}")
    void updatePullingStatusByMediaServerId(@Param("mediaServerId") String mediaServerId, @Param("pulling") boolean pulling);
    @Update("UPDATE wvp_stream_proxy " +
            "SET pulling=#{pulling} " +
            "WHERE id=#{id}")
    int updatePullingById(@Param("id") int id, @Param("pulling") boolean pulling);

    @Delete("DELETE FROM wvp_stream_proxy WHERE enable_remove_none_reader=true AND media_server_id=#{mediaServerId}")
    void deleteAutoRemoveItemByMediaServerId(String mediaServerId);

    @Select("SELECT st.* FROM wvp_stream_proxy st WHERE st.enable_remove_none_reader=true AND st.media_server_id=#{mediaServerId} order by st.create_time desc")
    List<StreamProxy> selectAutoRemoveItemByMediaServerId(String mediaServerId);

    @Select("select count(1) as total, sum(pulling) as online from wvp_stream_proxy")
    ResourceBaseInfo getOverview();

    @Select("select count(1) from wvp_stream_proxy")

    int getAllCount();

    @Select("select count(1) from wvp_stream_proxy where pulling = true")
    int getPulline();


    @Update({"<script>" +
            "<foreach collection='gpsMsgInfoList' item='item' separator=';'>" +
            " UPDATE" +
            " wvp_stream_proxy" +
            " SET longitude = #{item.lng}, latitude= #{item.lat}" +
            " WHERE gb_id=#{item.id}" +
            "</foreach>" +
            "</script>"})
    void updateStreamGPS(@Param("gpsMsgInfoList") List<GPSMsgInfo> gpsMsgInfoList);

    @Select("SELECT * from wvp_stream_proxy WHERE id=#{id}")
    StreamProxy selectOneById(@Param("id") int id);

    @Delete("delete from wvp_stream_proxy WHERE id=#{id}")
    void delById(int id);

}
