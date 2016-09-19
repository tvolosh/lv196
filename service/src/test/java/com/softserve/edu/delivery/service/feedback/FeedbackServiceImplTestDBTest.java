package com.softserve.edu.delivery.service.feedback;

import com.softserve.edu.delivery.domain.Feedback;
import com.softserve.edu.delivery.dto.FeedbackDTO;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Ivan Rudnytskyi on 15.09.2016.
 * <p>
 * test the methods of FeedbackServiceImpl.class
 */
public class FeedbackServiceImplTestDBTest {

    private static final long REQUIRED_NUMBERS_OF_FEEDBACKS = 10;

    @BeforeTest
    /**
     * checks, if table Feedback has enough entries, if not - populates it with the required number of feedbacks
     */
    private void checkIfTableHasEntries() {

        Long feedbackCount = FeedbackServiceImplTest.getFeedbackCount();

        if (feedbackCount < REQUIRED_NUMBERS_OF_FEEDBACKS)
            for (int i = 0; i < (REQUIRED_NUMBERS_OF_FEEDBACKS - feedbackCount); i++)
                FeedbackServiceImplTest.fsi.save(FeedbackServiceImplTest.createFeedbackDTOWithoutId());
    }

    /*
        first group of tests - on a real test db, included in the group testDB
     */

    @Test(enabled = false, groups = {"testDB"})
    /**
     * tests method from FeedbackServiceImpl.class, which copies fields from an object of Feedback.class
     * to an object of feedbackDTO.class
     */
    public void testCopyFeedbackToDTOTestDB() {

        Feedback feedback = FeedbackServiceImplTest.createFeedback();

        FeedbackDTO feedbackDTO = FeedbackServiceImplTest.fsi.copyFeedbackToDTO(feedback);

        Assert.assertTrue(feedback.getFeedbackId() == feedbackDTO.getFeedbackId() && feedback.getOrder() == feedbackDTO.getOrder()
                && feedback.getRate() == feedbackDTO.getRate() && feedback.getText() == feedbackDTO.getText() &&
                feedback.getUser() == feedbackDTO.getUser());

    }

    @Test(enabled = false, groups = {"testDB"})
    /**
     * tests method from FeedbackServiceImpl.class, which copies fields from an object of FeedbackDTO.class
     * to an object of Feedbac.class
     */
    public void testCopyDTOToFeedbackTestDB() {

        FeedbackDTO feedbackDTO = FeedbackServiceImplTest.createFeedbackDTO();

        Feedback feedback = FeedbackServiceImplTest.fsi.copyDTOToFeedback(feedbackDTO);

        Assert.assertTrue(feedback.getFeedbackId() == feedbackDTO.getFeedbackId() && feedback.getOrder() == feedbackDTO.getOrder()
                && feedback.getRate() == feedbackDTO.getRate() && feedback.getText() == feedbackDTO.getText() &&
                feedback.getUser() == feedbackDTO.getUser());
    }

    @Test(enabled = false, groups = {"testDB"})
    /**
     *  test#1 for the method from FeedbackServiceImpl.class, which get a list of objects of FeedbackDTO.class
     *  starting from startId, number of objects - startId + count
     *  the method check the size of the list
     */
    public void testgetAllFeedbacks0TestDB() {

        long count = FeedbackServiceImplTest.getFeedbackCount();

        List<FeedbackDTO> feedbackList = FeedbackServiceImplTest.fsi.getAllFeedbacks();

        Assert.assertTrue(count == feedbackList.size());

    }

    @Test(enabled = false, groups = {"testDB"})
    /**
     *  test#1 for the method from FeedbackServiceImpl.class, which get a list of objects of FeedbackDTO.class
     *  starting from startId, number of objects - startId + count
     *  the method check the contents of the list - objects must not be null and their type must be FeedbackDTO
     */
    public void testgetAllFeedbacks1TestDB() {

        List<FeedbackDTO> feedbackList = FeedbackServiceImplTest.fsi.getAllFeedbacks();

        boolean notEmpty = true;

        for (FeedbackDTO f : feedbackList) {
            if (f == null || !(f instanceof FeedbackDTO))
                notEmpty = false;
        }

        Assert.assertTrue(notEmpty);
    }

    @Test(enabled = false, groups = {"testDB"})
    /**
     *  test#1 for the method from FeedbackServiceImpl.class, which get a list of objects of FeedbackDTO.class
     *  starting from startId, number of objects - startId + count
     *  the method check the size of the list
     */
    public void testgetAllFeedbacksInRange0TestDB() {

        Long startId = FeedbackServiceImplTest.getStartFeedbackId();
        Long lastId = FeedbackServiceImplTest.getLastFeedbackId();

        long count = 5L;

        if ((startId + count) > lastId)
            count = lastId - startId;

        List<FeedbackDTO> feedbackList = FeedbackServiceImplTest.fsi.getAllFeedbacksInRange(startId.intValue(), (int) count);

        Assert.assertTrue(count == feedbackList.size());
    }

    @Test(enabled = false, groups = {"testDB"})
    /**
     *  test#1 for the method from FeedbackServiceImpl.class, which get a list of objects of FeedbackDTO.class
     *  starting from startId, number of objects - startId + count
     *  the method check the contents of the list - objects must not be null and their type must be FeedbackDTO
     */
    public void testgetAllFeedbacksInRange1TestDB() {

        Long startId = FeedbackServiceImplTest.getStartFeedbackId();

        long count = FeedbackServiceImplTest.getFeedbackCount();

        List<FeedbackDTO> feedbackList = FeedbackServiceImplTest.fsi.getAllFeedbacksInRange(startId.intValue(), (int) count);

        boolean notEmpty = true;

        for (FeedbackDTO f : feedbackList) {
            if (f == null || !(f instanceof FeedbackDTO))
                notEmpty = false;
        }

        Assert.assertTrue(notEmpty);
    }


    @Test(enabled = false, groups = {"testDB"})
    /**
     * tests method from FeedbackServiceImpl.class, which gets an object of FeedbackDTO.class with a given id
     *
     */
    public void testGetFeedbackByIdTestDB() {

        long feedbackId = FeedbackServiceImplTest.getRandomFeedbackId();

        FeedbackDTO feedbackDTO = FeedbackServiceImplTest.fsi.getFeedbackById(feedbackId);

        Assert.assertEquals(feedbackId, (long) feedbackDTO.getFeedbackId());
    }

    @Test(enabled = false, groups = {"testDB"})
    /**
     * tests method from FeedbackServiceImpl.class, which changes status of an feedback in the db
     */
    public void testChangeFeedbackStatusTestDB() {

        long feedbackId = FeedbackServiceImplTest.getRandomFeedbackId();

        FeedbackDTO feedbackDTO = FeedbackServiceImplTest.fsi.getFeedbackById(feedbackId);

        //retrieving actual status
        boolean previousStatus = feedbackDTO.isApproved();
        //changing the status to an opposite
        feedbackDTO.setApproved(!previousStatus);
        //updating the status
        FeedbackServiceImplTest.fsi.changeFeedbackStatus(feedbackDTO.getFeedbackId(), !previousStatus);
        //retrieving the updated object
        feedbackDTO = FeedbackServiceImplTest.fsi.getFeedbackById(feedbackId);

        Assert.assertFalse(previousStatus == feedbackDTO.isApproved());
    }

    @Test(enabled = false, groups = {"testDB"})
    /**
     * tests method from FeedbackServiceImpl.class, which saves an object of FeedbackDTO.class with a given id
     * to the db
     */
    public void testSaveTestDB() {

        //retrieving previous number of entries in the db
        long former = FeedbackServiceImplTest.getFeedbackCount();

        FeedbackServiceImplTest.fsi.save(FeedbackServiceImplTest.createFeedbackDTOWithoutId());

        //retrieving number of entries in the db after addind an entry
        long latter = FeedbackServiceImplTest.getFeedbackCount();

        Assert.assertTrue((latter - former) == 1);
    }

    @Test(enabled = false, groups = {"testDB"})
    /**
     * tests method from FeedbackServiceImpl.class, which updates an object of FeedbackDTO.class with a given id
     * to the db
     */
    public void testUpdateTestDB() {

        long feedbackId = FeedbackServiceImplTest.getRandomFeedbackId();

        //retrieving an object of FeedbackDTO.class from the db
        FeedbackDTO feedbackDTO0 = FeedbackServiceImplTest.fsi.getFeedbackById(feedbackId);

        feedbackDTO0 = FeedbackServiceImplTest.changeData(feedbackDTO0);

        //updating it
        FeedbackServiceImplTest.fsi.update(feedbackDTO0);

        //retrieving the same object of FeedbackDTO.class from the db
        FeedbackDTO feedbackDTO1 = FeedbackServiceImplTest.fsi.getFeedbackById(feedbackId);

        //comparing fields of the objects
        Assert.assertTrue(feedbackDTO0.getFeedbackId().equals(feedbackDTO1.getFeedbackId()) &&
                feedbackDTO0.getRate().equals(feedbackDTO1.getRate()) &&
                feedbackDTO0.getText().equals(feedbackDTO1.getText()) &&
                feedbackDTO0.isApproved().equals(feedbackDTO1.isApproved()));
    }

    @Test(enabled = false, groups = {"testDB"}, expectedExceptions = NoSuchElementException.class)
    /**
     * tests method from FeedbackServiceImpl.class, which deletes an object of FeedbackDTO.class with a given id
     * from the db
     */

    public void testDeleteTestDB() {

        long feedbackId = FeedbackServiceImplTest.getRandomFeedbackId();

        FeedbackDTO feedbackDTO = FeedbackServiceImplTest.fsi.getFeedbackById(feedbackId);

        FeedbackServiceImplTest.fsi.delete(feedbackDTO.getFeedbackId());

        FeedbackServiceImplTest.fsi.getFeedbackById(feedbackId);
    }

    @Test(enabled = false, groups = {"testDB"})
    /**
     * tests method from FeedbackServiceImpl.class, which looks in the db for an object of FeedbackDTO.class
     * with a given id
     */
    public void testFindOneTestDB() {

        long feedbackId = FeedbackServiceImplTest.getRandomFeedbackId();

        Assert.assertNotNull(FeedbackServiceImplTest.fsi.findOne(feedbackId));
    }
}
