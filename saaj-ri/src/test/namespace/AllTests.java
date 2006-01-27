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
 * Test to reproduce and hopefully fix the namespace bug.
 *
 * 1. xmlns should not be allowed to be redefined.
 * 2. Two attributes with same name cannot exist in an element. 
 *    The attribute names are not namespace qualified, so the 
 *    exact names would have to match.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 */
package namespace;

import junit.framework.*;

public class AllTests extends TestCase {

    public AllTests(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        
        // testing the fix made locally in our implementation
        // throwing exception before sending it to dom4j
        
        suite.addTestSuite(NamespaceTest.class);
        suite.addTestSuite(NSDeclTest.class);     
        suite.addTestSuite(DefaultNamespaceTest.class);     
        suite.addTestSuite(GetNamespaceURITest.class);     
        return suite;
    }
}
