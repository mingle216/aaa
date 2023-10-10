package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
<#if enableCache>
import com.wisedu.minos.casp.portal.utils.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
</#if>

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
<#if enableCache>
@CacheNamespace(implementation = MybatisRedisCache.class,eviction=MybatisRedisCache.class)
</#if>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
