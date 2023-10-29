package com.hansol.board.mock;

import com.hansol.board.common.PageSetting;
import com.hansol.board.exception.NoPostException;
import com.hansol.board.exception.PasswordErrorException;
import com.hansol.board.post.domain.Post;
import com.hansol.board.post.domain.PostEntity;
import com.hansol.board.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class FakePostRepository implements PostRepository {
    private Long id = 0L;
    private final List<PostEntity> posts = new ArrayList<>();
    @Override
    public PostEntity findById(Long postId) {
        return posts.stream().filter(post -> post.getId().equals(postId)).findAny()
                .orElseThrow(NoPostException::new);
    }

    @Override
    public PostEntity save(PostEntity post) {
        if (post.getId() == null || post.getId() == 0L) {
            post.setId(++id);
            posts.add(post);
        } else {
            throw new IllegalArgumentException("잘못된 postId 입니다.");
        }

        return post;
    }

    @Override
    public PostEntity update(PostEntity post) {
        posts.removeIf(p -> p.getId().equals(post.getId()));
        posts.add(post);
        return post;
    }

    @Override
    public PostEntity findByIdAndAndPassword(Long id, String password) {
        return posts.stream()
                .filter(p -> p.getId().equals(id) && p.getSecret().equals(true) && p.getPassword().equals(password))
                .findAny()
                .orElseThrow(PasswordErrorException::new);
    }

    @Override
    public Page<PostEntity> findListOrderby(int page, String code) {
        posts.sort(Comparator
                .comparing(PostEntity::getNotice).reversed());

        Pageable pageable = PageSetting.getPostPageable(page);

        List<PostEntity> list = posts.stream().toList();
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public void remove(Long id, String password) {
        if (passwordCheck(id, password)) {
            posts.removeIf(p -> p.getId().equals(id) && p.getPassword().equals(password));
        } else {
            throw new PasswordErrorException();
        }
    }

    @Override
    public Page<PostEntity> findByConditions(String condition, String search) {
        List<PostEntity> matchingPosts = new ArrayList<>();
        for(PostEntity post : posts){
            if ("all".equals(condition) || "title".equals(condition)) {
                if (post.getTitle() != null && post.getTitle().contains(search)) {
                    matchingPosts.add(post);
                    continue;
                }
            }
            if ("all".equals(condition) || "content".equals(condition)) {
                if (post.getTitle() != null && post.getContent().contains(search)) {
                    matchingPosts.add(post);
                    continue;
                }
            }
            if ("all".equals(condition) || "writer".equals(condition)) {
                if (post.getWriter() != null && post.getWriter().contains(search)) {
                    matchingPosts.add(post);
                }
            }
        }

        Pageable pageable = PageSetting.getPostPageable(0);
        return new PageImpl<>(matchingPosts, pageable, matchingPosts.size());
    }

    @Override
    public Boolean passwordCheck(Long id, String password) {
        for (PostEntity p : posts) {
            if (p.getId().equals(id) && p.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isSecretPost(Long id) {
        PostEntity findPost = findById(id);
        return findPost.getSecret();
    }

    @Override
    public List<PostEntity> findAll(int page, String code) {
        posts.sort(Comparator
                .comparing(PostEntity::getNotice).reversed()
                .thenComparing(PostEntity::getCreatedDate));
        return posts;
    }

    @Override
    public PostEntity findPrevPost(Long id) {
        PostEntity post = findById(id);

        int index = posts.indexOf(post);
        for (int i = index-1; i >= 0; i--) {
            if(!posts.get(i).getSecret()) return posts.get(i);
        }
        return null;
    }

    @Override
    public PostEntity findNextPost(Long id) {
        PostEntity post = findById(id);

        int index = posts.indexOf(post);
        for (int i = index+1; i < posts.size(); i++) {
            if(!posts.get(i).getSecret()) return posts.get(i);
        }
        return null;
    }

    @Override
    public Optional<Post> checkPassword(Long id, String password) {
        return Optional.empty();
    }
}
