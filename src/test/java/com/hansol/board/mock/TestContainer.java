package com.hansol.board.mock;

import com.hansol.board.boardInfo.repository.BoardInfoRepository;
import com.hansol.board.comment.repository.CommentRepository;
import com.hansol.board.member.repository.MemberRepository;
import com.hansol.board.member.service.MemberService;
import com.hansol.board.member.service.MemberServiceImpl;
import com.hansol.board.post.repository.PostRepository;
import com.hansol.board.post.service.PostService;
import com.hansol.board.post.service.PostServiceImpl;
import lombok.Builder;

public class TestContainer {
    public final PostRepository postRepository;
    public final MemberRepository memberRepository;
    public final CommentRepository commentRepository;
    public final BoardInfoRepository boardInfoRepository;
    public final PostService postService;
    public final MemberService memberService;

    @Builder
    public TestContainer() {
        this.postRepository = new FakePostRepository();
        this.memberRepository = new FakeMemberRepository();
        this.commentRepository = new FakeCommentRepository();
        this.boardInfoRepository = new FakeBoardInfoRepository();
        this.postService = new PostServiceImpl(postRepository);
        this.memberService = new MemberServiceImpl(memberRepository);
    }
    public static TestContainer create() {
        return new TestContainer();
    }
}
