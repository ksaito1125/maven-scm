package org.apache.maven.scm.provider.git.gitexe.command.status;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.DefaultLog;
import org.codehaus.plexus.PlexusTestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public class GitStatusConsumerTest
    extends PlexusTestCase
{
    
    public void testConsumerAddedFile() 
    {
        GitStatusConsumer consumer = new GitStatusConsumer( new DefaultLog(), null );
        
        consumer.consumeLine( "#    new file:   project.xml" );
        
        List changedFiles = consumer.getChangedFiles();
        
        assertNotNull( changedFiles );
        assertEquals( 1, changedFiles.size() );
    }
    
    public void testLog1Consumer()
    throws Exception
    {
        GitStatusConsumer consumer = new GitStatusConsumer( new DefaultLog(), null );

        File f = getTestFile( "/src/test/resources/git/status/gitstatus1.log" );

        BufferedReader r = new BufferedReader( new FileReader( f ) );

        String line;

        while ( ( line = r.readLine() ) != null )
        {
            consumer.consumeLine( line );
        }

        List changedFiles = consumer.getChangedFiles();
        
        assertEquals( 2, changedFiles.size() );

        testScmFile( (ScmFile) changedFiles.get( 0 ), "project.xml", ScmFileStatus.ADDED );
        testScmFile( (ScmFile) changedFiles.get( 1 ), "readme.txt" , ScmFileStatus.MODIFIED );
   }
 
    public void testEmptyLogConsumer()
    throws Exception
    {
        GitStatusConsumer consumer = new GitStatusConsumer( new DefaultLog(), null );

        File f = getTestFile( "/src/test/resources/git/status/gitstatus-empty.log" );

        BufferedReader r = new BufferedReader( new FileReader( f ) );

        String line;

        while ( ( line = r.readLine() ) != null )
        {
            consumer.consumeLine( line );
        }

        List changedFiles = consumer.getChangedFiles();
        
        assertEquals( 0, changedFiles.size() );
   }
 
    
    public void testLog2Consumer()
    throws Exception
    {
        GitStatusConsumer consumer = new GitStatusConsumer( new DefaultLog(), null );

        File f = getTestFile( "/src/test/resources/git/status/gitstatus2.log" );

        BufferedReader r = new BufferedReader( new FileReader( f ) );

        String line;

        while ( ( line = r.readLine() ) != null )
        {
            consumer.consumeLine( line );
        }

        List changedFiles = consumer.getChangedFiles();
        
        assertEquals( 4, changedFiles.size() );

        testScmFile( (ScmFile) changedFiles.get( 0 )
                   , "maven-scm-provider-gitexe/src/main/java/org/apache/maven/scm/provider/git/gitexe/command/add/GitAddCommand.java"
                   , ScmFileStatus.MODIFIED );
        testScmFile( (ScmFile) changedFiles.get( 1 )
                   , "maven-scm-provider-gitexe/src/main/java/org/apache/maven/scm/provider/git/gitexe/command/checkin/GitCheckInCommand.java" 
                   , ScmFileStatus.MODIFIED );
        testScmFile( (ScmFile) changedFiles.get( 2 )
                     , "maven-scm-provider-gitexe/src/main/java/org/apache/maven/scm/provider/git/gitexe/command/checkin/GitCheckInConsumer.java" 
                     , ScmFileStatus.DELETED );
        testScmFile( (ScmFile) changedFiles.get( 3 )
                     , "maven-scm-provider-gitexe/src/main/java/org/apache/maven/scm/provider/git/gitexe/command/status/GitStatusConsumer.java"
                     , ScmFileStatus.MODIFIED );
   }
 
    
    private void testScmFile( ScmFile fileToTest, String expectedFilePath, ScmFileStatus expectedStatus )
    {
        assertEquals( expectedFilePath, fileToTest.getPath() );
        assertEquals( expectedStatus, fileToTest.getStatus() );
    }
 
}