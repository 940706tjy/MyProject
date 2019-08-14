package cn.bulaomeng.fragment.mapper;

import cn.bulaomeng.fragment.entity.Fragment;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FragmentMapper {
    List<Fragment> selectAll();

    int insert(Fragment record);

    List<Fragment> selectByPrimaryKey(Fragment record);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Fragment record);

    int updateByPrimaryKeySelective(Fragment record);

    int updateByPrimaryKey(Fragment record);

    List<String> getListByArray(@Param("name")String name);

    List<Map<String,Object>> selectByPr();

}