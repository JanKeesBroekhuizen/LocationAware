package com.dlvjkb.locationaware.integratietests;

import com.dlvjkb.locationaware.database.DatabaseManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.mock;

public class DatabaseManagerIntegratieTest {

    @Mock
    private DatabaseManager databaseManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        databaseManager = mock(DatabaseManager.class);
    }

    @Test
    public void getInstance(){
        
    }
}
