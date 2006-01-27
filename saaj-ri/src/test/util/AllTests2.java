/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * Code modified from AllTests.java found at
 * http:groups.yahoo.com/group/junit.  The name was changed to AllTests2
 * b/c AllTests conflicts w/ existing name used in "run-tests" ant target.
 * [eeg 19dec02]  This ended up being a gross hack, but it works.
 */
//package junit.contrib;
package util;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests2 extends TestSuite
{
	public static TestSuite suite() { 
		return new AllTests2(); 
	}
	
	public AllTests2()
	{
		try {
			String classPath = System.getProperty( "java.class.path" );
			String separator = System.getProperty( "path.separator" );
			gatherFiles( splitClassPath( classPath, separator ) );
		} catch ( Throwable unexpected ) {
			unexpected.printStackTrace();
		}
	}
	
	private void gatherFiles( List roots )
	{
		Iterator i = roots.iterator();
		while ( i.hasNext() ) {
			gatherFiles( new File( (String) i.next() ), "" );
		}
	}

	private void gatherFiles( File classRoot, String classFilename )
	{
		File thisRoot = new File( classRoot, classFilename );
		if ( thisRoot.isFile() ) {
                    // [eeg19dec02]
                    if (System.getProperty("saaj.run.only.AllTests") != null) {
                        if (classFilename != null &&
                            (classFilename.equals("AllTests.class") || 
                             classFilename.endsWith(
                                 File.separatorChar + "AllTests.class"))) {
                            maybeAddTestFor2( classFilename );
                        }
                    } else {
			maybeAddTestFor( classFilename );
                    }
                    return;
		}
		
		String[] contents = thisRoot.list();
		for ( int i = 0; i < contents.length; i++ )
		{
			gatherFiles( classRoot, classFilename + File.separatorChar + contents[i] );
		}				
	}

    // [eeg19dec02] Hack
    private void maybeAddTestFor2( String classFileName )
    {
        if ( !classFileName.endsWith( ".class" ) )
            return;
        try
            {
                Class testClass = classFromFile( classFileName );
                if ( isTest( testClass ) && testClass != this.getClass() )
                    {
                        Test tests = null;
                        try {
                            Method m = testClass.getMethod("suite", null);
                            tests = (Test) m.invoke(null, null);
                        } catch (Exception ex) {
                            throw new RuntimeException(
                                "Unexpected Exception: " + ex);
                        }
                        addTest(tests);
                    }
            } 
        catch ( ClassNotFoundException expected ) {;}
        catch ( NoClassDefFoundError notFatal ) {
            System.err.println( "Class not loaded for " + classNameFromFile( classFileName ) + 
				" (" + notFatal.getMessage() + " not found)" );
        } 
    }
	
	private static List splitClassPath( String classPath, String separator )
	{
		List result = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(classPath, separator);
		while ( tokenizer.hasMoreTokens()) {
			result.add( tokenizer.nextToken() );
		}
		return result;
	}
	
	/****************************************************************************
	*/
	private void maybeAddTestFor( String classFileName )
	{
		if ( !classFileName.endsWith( ".class" ) )
			return;
	    try
		{
			Class testClass = classFromFile( classFileName );
			if ( isTest( testClass ) && testClass != this.getClass() )
			{
				TestSuite suite = new TestSuite( testClass );
				addTest( suite );
			}
		} 
		catch ( ClassNotFoundException expected ) {;}
		catch ( NoClassDefFoundError notFatal ) {
			System.err.println( "Class not loaded for " + classNameFromFile( classFileName ) + 
				" (" + notFatal.getMessage() + " not found)" );
		} 
	}

	private static boolean isTest( Class c ) {
		return junit.framework.Test.class.isAssignableFrom( c );
	}	
	
	private static Class classFromFile( String classFileName ) throws ClassNotFoundException 
	{
		return Class.forName( classNameFromFile( classFileName ) );
	}

	private static String classNameFromFile( String classFileName )
	{
		String clean = trimTrailingDotClass( trimLeadingFileSeparator( classFileName ) );
		return clean.replace( File.separatorChar, '.' );
	}
	
	private static String trimLeadingFileSeparator( String s )
	{
		if ( s.charAt( 0 ) == File.separatorChar )
			return s.substring( 1 );
		else 
			return s;
	}
	
	private static String trimTrailingDotClass( String s )
	{
		return s.substring( 0, s.length() - ".class".length() );		
	}
	
	
    /* The following tests are platform dependent so disable them [eeg 18dec02]
public static void main( String args[] )
{
    junit.textui.TestRunner.run( Test.class );
}

public static class Test extends junit.framework.TestCase
{
	public void testClassFromFile() throws ClassNotFoundException 
	{
		String classFilename = "\\junit\\contrib\\AllTests$Test.class";
		assertEquals( "junit.contrib.AllTests$Test", classNameFromFile( classFilename ) );
		assertSame( this.getClass(), classFromFile( classFilename ) );
	}
	
	public void testSplitClassPath()
	{
		List dirs = splitClassPath( "c:/here;.;there;d:/everywhere/else", ";" );
		int i = 0;
		assertEquals( "c:/here", dirs.get( i++ ) );
		assertEquals( ".", dirs.get( i++ ) );
		assertEquals( "there", dirs.get( i++ ) );
		assertEquals( "d:/everywhere/else", dirs.get( i++ ) );
	}
	
    public Test( String testName ) { super( testName ); }
}
*/
	
	
}
