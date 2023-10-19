package com.hansol.board.mock;

import com.hansol.board.comment.repository.CommentRepository;
import com.hansol.board.member.repository.MemberRepository;
import com.hansol.board.post.repository.PostRepository;
import com.hansol.board.post.service.PostService;
import lombok.Builder;

public class TestContainer {
    public final PostRepository postRepository;
    public final MemberRepository memberRepository;
    public final CommentRepository commentRepository;
    public final PostService postService;

    @Builder
    public TestContainer() {
        this.postRepository = new FakePostRepository();
        this.memberRepository = new FakeMemberRepository();
        this.commentRepository = new FakeCommentRepository();
        this.postService = new FakePostService(postRepository);
    }
    public static TestContainer create() {
        return new TestContainer();
    }
}
