/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/*
 * $Id: TranslationService.java,v 1.5 2009-01-17 00:39:49 ramapulavarthi Exp $
 * $Revision: 1.5 $
 * $Date: 2009-01-17 00:39:49 $
 */


package translator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Properties;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * Translation Service that talks to Babelfish.altavista.com and gets back translations.
 *
 * @author Manveen Kaur (manveen.kaur@sun.com)
 *
 */

public class TranslationService {

    private String translation = "";
    private String proxyHost = "";
    private String proxyPort = "";

    public static final String ENGLISH_TO_GERMAN = "en_de";
    public static final String ENGLISH_TO_FRENCH = "en_fr";
    public static final String ENGLISH_TO_ITALIAN = "en_it";
    public static final String ENGLISH_TO_SPANISH = "en_es";

    public TranslationService() {

    }

    public TranslationService(String host, String port) {
        this.proxyHost = host;
        this.proxyPort = port;
        Properties props = System.getProperties();
        props.put("http.proxyHost", host);
        props.put("http.proxyPort", port);
    }

    public String translate(String translate_text, String toLanguage) {

        // Open a connection with the BabelFish URL.
        // Get the encoded String back.
        try {

            String text = URLEncoder.encode(translate_text);

            //HTTP Get.
            URL url = new URL("http://babelfish.altavista.com/babelfish/tr"
            +"?urltext=" + text
            + "&lp="+ toLanguage);

            URLConnection con = url.openConnection();

            BufferedReader in = new BufferedReader(new
            InputStreamReader(con.getInputStream()));

            ParserDelegator parser = new ParserDelegator();

            HTMLEditorKit.ParserCallback callback =
            new HTMLEditorKit.ParserCallback() {

                // the translation will be in the div tag
                private boolean end_search = false;
                private boolean found_first_textarea = false;

                public void handleText(char[] data, int pos) {
                    if (found_first_textarea) {
                        translation = new String(data);
                    }
                }

                public void handleStartTag(HTML.Tag tag,
                MutableAttributeSet attrSet, int pos) {
                    if (tag == HTML.Tag.DIV  && end_search != true) {
                        found_first_textarea = true;
                    }
                }

                public void handleEndTag(HTML.Tag t, int pos) {
                    if (t == HTML.Tag.DIV && end_search != true) {
                        end_search = true;
                        found_first_textarea = false;
                    }
                }
            };

            parser.parse(in, callback , true);
            in.close();

        } catch (UnknownHostException uhe) {
            System.out.println("Exception: " + uhe.getMessage());
            System.out.println("If using proxy, please make sure " +
            "your proxy settings are correct." );
            System.out.println("Proxy Host = " + proxyHost +
            "\nProxy Port = " + proxyPort);
            uhe.printStackTrace();
        } catch (Exception me) {
            System.out.println("Exception: " + me.getMessage());
            me.printStackTrace();
        }
        return translation;
    }
}


