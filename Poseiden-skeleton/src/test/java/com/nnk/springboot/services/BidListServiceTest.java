package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListService;

    private BidList bid1;
    private BidList bid2;

    @BeforeEach
    void setUp() {
        bid1 = new BidList();
        bid1.setId(1);
        bid1.setAccount("Account1");
        bid1.setType("Type1");
        bid1.setBidQuantity(10.0);

        bid2 = new BidList();
        bid2.setId(2);
        bid2.setAccount("Account2");
        bid2.setType("Type2");
        bid2.setBidQuantity(20.0);
    }

    @Test
    void testFindAllBids() {
        // Arrange
        when(bidListRepository.findAll()).thenReturn(Arrays.asList(bid1, bid2));

        // Act
        List<BidList> bids = bidListService.findAllBids();

        // Assert
        assertThat(bids).hasSize(2);
        verify(bidListRepository, times(1)).findAll();
    }

    @Test
    void testSaveBid() {
        when(bidListRepository.save(any(BidList.class))).thenReturn(bid1);

        BidList savedBids = bidListService.saveBid(bid1);

        assertNotNull(savedBids);
        verify(bidListRepository, times(1)).save(any(BidList.class));
    }

    @Test
    void testFindById_Success() throws Exception {
        // Arrange
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid1));

        // Act
        BidList foundBid = bidListService.findById(1);

        // Assert
        assertThat(foundBid).isEqualTo(bid1);
        verify(bidListRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(bidListRepository.findById(3)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> bidListService.findById(3));
        assertThat(exception.getMessage()).isEqualTo("can't retrieve Bid with id 3");
        verify(bidListRepository, times(1)).findById(3);
    }

    @Test
    void testUpdateBidList_Success() throws Exception {
        // Arrange
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid1)); 

        // Changer des valeurs pour la mise à jour
        BidList updatedBid = new BidList();
        updatedBid.setId(1);
        updatedBid.setAccount("UpdatedAccount");
        updatedBid.setType("UpdatedType");
        updatedBid.setBidQuantity(50.0);
        
        when(bidListRepository.save(any(BidList.class))).thenReturn(updatedBid);

        // Act
        BidList result = bidListService.updateBidList(updatedBid);

        // Assert
        assertThat(result.getAccount()).isEqualTo("UpdatedAccount");
        assertThat(result.getType()).isEqualTo("UpdatedType");
        assertThat(result.getBidQuantity()).isEqualTo(50.0);
        verify(bidListRepository, times(1)).findById(1);
        verify(bidListRepository, times(1)).save(any(BidList.class));
    }

    @Test
    void testUpdateBidList_NotFound() {
        // Arrange
        BidList nonExistingBid = new BidList();
        nonExistingBid.setId(1); // Assurez-vous que l'ID correspond à l'appel réel
        nonExistingBid.setAccount("NewAccount");
        nonExistingBid.setType("NewType");
        nonExistingBid.setBidQuantity(50.0);

        when(bidListRepository.findById(1)).thenReturn(Optional.empty());


        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> bidListService.updateBidList(bid1));
        assertThat(exception.getMessage()).isEqualTo("Can't find current Bid");
        
    }

    @Test
    void testDeleteBidById_Success() throws Exception {
        // Arrange
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid1));
        doNothing().when(bidListRepository).deleteById(1);

        // Act
        bidListService.deleteBidById(1);

        // Assert
        verify(bidListRepository, times(1)).findById(1);
        verify(bidListRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBidById_NotFound() {
        // Arrange
        when(bidListRepository.findById(3)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> bidListService.deleteBidById(3));
        assertThat(exception.getMessage()).isEqualTo("Can't find the BidList for id: 3");
        verify(bidListRepository, times(1)).findById(3);
    }
}
