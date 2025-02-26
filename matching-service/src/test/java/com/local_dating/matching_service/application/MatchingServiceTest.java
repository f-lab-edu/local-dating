package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.Matching;
import com.local_dating.matching_service.domain.vo.MatchingVO;
import com.local_dating.matching_service.infrastructure.repository.MatchingeRepository;
import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import com.local_dating.matching_service.util.UserServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @Mock
    private MatchingeRepository matchingeRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private MatchingVO matchingVO;

    @Mock
    private Matching matching;

    @InjectMocks
    private MatchingService matchingService;

    public MatchingServiceTest() {

    }

    /*@Test
    void viewCoinSuccess() {

        long userid = 1L;
        String authentication = "1234567890";
        MatchingVO matchingVO = new MatchingVO(1, 2, "000", "000", "000", "00", "20250505", "1830");

        when(userServiceClient.viewCoin(userid, authentication)).thenReturn(20000L);
        //matchingService.requestMatching(userid, authentication, matchingVO);
        int result = matchingService.requestMatching(userid, authentication, matchingVO);

        assertEquals(0, result);
        verify(userServiceClient).saveCoin(eq(userid), eq(authentication), any(UserCoinDTO.class));
        verify(matchingeRepository).save(any(Matching.class));
    }*/

    @Test
    void viewCoinSuccess() {

        long userid = 1L;
        String authentication = "1234567890";
        MatchingVO matchingVO = new MatchingVO(1, 2, "000", "000", "000", "00", "20250505", "1830");

        when(userServiceClient.viewCoin(userid, authentication)).thenReturn(20000L);
        matchingService.requestMatching(userid, authentication, matchingVO);

        verify(userServiceClient).saveCoin(eq(userid), eq(authentication), any(UserCoinDTO.class));
        verify(matchingeRepository).save(any(Matching.class));
    }

/*    @Test
    void viewCoinFail() {

        long userid = 1L;
        String authentication = "1234567890";
        MatchingVO matchingVO = new MatchingVO(1, 2, "000", "000", "000", "00", "20250505", "1830");

        when(userServiceClient.viewCoin(userid, authentication)).thenReturn(0L);
        int result = matchingService.requestMatching(userid, authentication, matchingVO);

        assertEquals(-1, result);
        verify(userServiceClient, never()).saveCoin(eq(userid), eq(authentication), any(UserCoinDTO.class));
        verify(matchingeRepository, never()).save(any(Matching.class));
    }*/

}