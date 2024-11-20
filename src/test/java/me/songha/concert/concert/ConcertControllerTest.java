package me.songha.concert.concert;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.songha.concert.api.ConcertController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ConcertController.class)
class ConcertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConcertRepositoryService concertRepositoryService;

    private List<ConcertDto> concertList;

    @BeforeEach
    void setUp() {
        concertList = Arrays.asList(
                ConcertDto.builder()
                        .id(1L)
                        .title("Title 1")
                        .description("Description 1")
                        .createdAt(LocalDateTime.now()).build(),
                ConcertDto.builder()
                        .id(2L)
                        .title("Title 2")
                        .description("Description 2")
                        .createdAt(LocalDateTime.now()).build()
        );
    }

    @DisplayName("concert를 page 처리하여 반환을 테스트한다.")
    @Test
    public void getAllConcerts() throws Exception {
        Page<ConcertDto> concertPage = new PageImpl<>(concertList);
        Mockito.when(concertRepositoryService.getAllConcerts(PageRequest.of(0, 10))).thenReturn(concertPage);

        mockMvc.perform(get("/concert/all")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Title 1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].title").value("Title 2"));

        Mockito.verify(concertRepositoryService, Mockito.times(1)).getAllConcerts(PageRequest.of(0, 10));
    }

    @DisplayName("concert를 id로 반환한다.")
    @Test
    void getConcert() throws Exception {
        Long concertId = 1L;
        ConcertDto concertDto = concertList.getFirst();
        Mockito.when(concertRepositoryService.getConcert(concertId)).thenReturn(concertDto);

        mockMvc.perform(get("/concert/{id}", concertId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title 1"))
                .andExpect(jsonPath("$.description").value("Description 1"));
    }

    @DisplayName("concert를 생성한다.")
    @Test
    void createConcert() throws Exception {
        ConcertDto concertDto = ConcertDto.builder()
                .title("Title 3")
                .description("Description 3")
                .build();

        mockMvc.perform(post("/concert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concertDto)))
                .andExpect(status().isCreated());
        Mockito.verify(concertRepositoryService).createConcert(any(ConcertDto.class));
    }

    @DisplayName("concert를 업데이트한다.")
    @Test
    void updateConcert() throws Exception {
        Long concertId = 1L;
        ConcertDto concertDto = concertList.getFirst();

        mockMvc.perform(put("/concert/{id}", concertId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concertDto)))
                .andExpect(status().isOk());
        Mockito.verify(concertRepositoryService).updateConcert(eq(concertId), any(ConcertDto.class));
    }

    @DisplayName("concert를 삭제한다.")
    @Test
    void deleteConcert() throws Exception {
        Long concertId = 1L;

        mockMvc.perform(delete("/concert/{id}", concertId))
                .andExpect(status().isNoContent());
        Mockito.verify(concertRepositoryService).deleteConcert(concertId);
    }
}
