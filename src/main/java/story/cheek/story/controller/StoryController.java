package story.cheek.story.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import story.cheek.common.constant.SortType;
import story.cheek.member.domain.Member;
import story.cheek.security.CurrentMember;
import story.cheek.story.dto.request.StoryCreateRequest;
import story.cheek.story.dto.request.StoryCreateRequestWithoutImage;
import story.cheek.story.dto.response.StoryDetailResponse;
import story.cheek.story.dto.response.StoryResponse;
import story.cheek.story.service.StoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stories")
public class StoryController {

    private final StoryService storyService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> create(
            @CurrentMember Member member,
            @RequestPart StoryCreateRequestWithoutImage request,
            @RequestPart MultipartFile image
            ) {

        StoryCreateRequest storyCreateRequest = StoryCreateRequest.of(request, image);
        Long storyId = storyService.save(member, storyCreateRequest);

        return ResponseEntity.created(URI.create("/api/v1/stories/" + storyId))
                .build();
    }

    @GetMapping("/{storyId}")
    public ResponseEntity<StoryDetailResponse> findById(
            @CurrentMember Member member,
            @PathVariable Long storyId
    ) {

        StoryDetailResponse response = storyService.findDetailById(member, storyId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Slice<StoryResponse>> findAll(
            @CurrentMember Member member,
            @PageableDefault(size = 10) Pageable pageable,
            SortType sortType
    ) {

        Slice<StoryResponse> response = storyService.findAll(member, pageable, sortType);

        return ResponseEntity.ok(response);
    }
}
