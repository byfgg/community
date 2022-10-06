package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author byfgg
 * @create 2022-09-16 15:13
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchCrudRepository<DiscussPost, Integer> {

}
