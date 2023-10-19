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
    private final List<Post> posts = new ArrayList<>();
    @Override
    public Optional<Post> findById(Long postId) {
        return Optional.ofNullable(posts.stream().filter(post -> post.getId().equals(postId)).findAny()
                .orElseThrow(NoPostException::new));
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null || post.getId() == 0L) {
            post.setId(++id);
            posts.add(post);
        } else {
            throw new IllegalArgumentException("잘못된 postId 입니다.");
        }

        return post;
    }

    @Override
    public Post update(Post post, String password) {
        if (passwordCheck(post.getId(), password)) {
            posts.removeIf(p -> p.getId().equals(post.getId()));
            posts.add(post);
        } else {
            throw new PasswordErrorException();
        }
        return post;
    }

    @Override
    public Optional<Post> findByIdAndAndPassword(Long id, String password) {
        return Optional.ofNullable(posts.stream()
                .filter(p -> p.getId().equals(id) && p.getSecret().equals(true) && p.getPassword().equals(password))
                .findAny()
                .orElseThrow(PasswordErrorException::new));
    }

    @Override
    public Page<Post> findListOrderby(int page, String code) {
        posts.sort(Comparator
                .comparing(Post::getNotice).reversed()
                .thenComparing(Post::getCreatedDate));

        Pageable pageable = PageSetting.getPostPageable(page);

        return new PageImpl<>(posts, pageable, posts.size());
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
    public Page<Post> findByConditions(String condition, String search) {
        List<Post> matchingPosts = new ArrayList<>();
        for(Post post : posts){
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
                if (post.getWriter() != null && post.getWriter().getName().contains(search)) {
                    matchingPosts.add(post);
                }
            }
        }

        Pageable pageable = PageSetting.getPostPageable(0);
        return new PageImpl<>(matchingPosts, pageable, matchingPosts.size());
    }

    @Override
    public Boolean passwordCheck(Long id, String password) {
        for (Post p : posts) {
            if (p.getId().equals(id) && p.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isSecretPost(Long id) {
        Optional<Post> findPost = findById(id);
        if (findPost.isPresent()) {
            return findPost.get().getSecret();
        } else {
            throw new IllegalArgumentException("잘못된 postId 입니다.");
        }
    }

    @Override
    public List<Post> findAll(int page, String code) {
        posts.sort(Comparator
                .comparing(Post::getNotice).reversed()
                .thenComparing(Post::getCreatedDate));
        return posts;
    }

    @Override
    public Optional<Post> findPrevPost(Long id) {
        Optional<Post> post = findById(id);

        if (post.isPresent()) {
            int index = posts.indexOf(post.get());
            for (int i = index-1; i >= 0; i--) {
                if(!posts.get(i).getSecret()) return Optional.ofNullable(posts.get(i));
            }
        } else {
            throw new IllegalArgumentException("잘못된 postId 입니다.");
        }
        return null;
    }

    @Override
    public Optional<Post> findNextPost(Long id) {
        Optional<Post> post = findById(id);

        if (post.isPresent()) {
            int index = posts.indexOf(post.get());
            for (int i = index+1; i < posts.size(); i++) {
                if(!posts.get(i).getSecret()) return Optional.ofNullable(posts.get(i));
            }
        } else {
            throw new IllegalArgumentException("잘못된 postId 입니다.");
        }
        return null;
    }
}
