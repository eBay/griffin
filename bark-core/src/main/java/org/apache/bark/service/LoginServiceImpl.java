/*
	Copyright (c) 2016 eBay Software Foundation.
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.apache.bark.service;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;

@Service
//@Validated
@PropertySource("classpath:application.properties")
public class LoginServiceImpl implements LoginService {
	final static Logger logger = LoggerFactory
			.getLogger(LoginServiceImpl.class);

	@Autowired
	private Environment env;

	@Override
	public String login(String ntUser, String password) {
		String ldapServer = env.getProperty("ldap.server");
		int ldapPort = Integer.parseInt(env.getProperty("ldap.port"));
		String ldapUser = ntUser + "@corp.ebay.com";
		String ldapPass = password;

		String ldapFactory = "com.sun.jndi.ldap.LdapCtxFactory";
		String ldapUrl = "ldap://" + ldapServer + ":" + ldapPort;

		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
		ht.put("com.sun.jndi.ldap.connect.timeout", "5000");
		ht.put("com.sun.jndi.ldap.read.timeout", "5000");
		// LDAP server
		ht.put(Context.PROVIDER_URL, ldapUrl);

		ht.put(Context.SECURITY_PRINCIPAL, ldapUser);
		ht.put(Context.SECURITY_CREDENTIALS, ldapPass);

		LdapContext ctx;
		try {
			ctx = new InitialLdapContext(ht, null);

			String ldapSearchBase = "DC=corp,DC=ebay,DC=com";

			String searchFilter = "(sAMAccountName=" + ntUser + ")";

			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration<SearchResult> results = ctx.search(
					ldapSearchBase, searchFilter, searchControls);

			SearchResult searchResult = null;
			while (results.hasMoreElements()) {
				searchResult = results.nextElement();
				Attributes attrs = searchResult.getAttributes();

				// TODO::Not sure whether the attribute id will be changed in
				// the future, currently it's extensionAttribute8
				// String quid = (String)
				// attrs.get("extensionAttribute8").get();
				if (attrs != null && attrs.get("cn") != null) {
					String cnName = (String) attrs.get("cn").get();
					String fullName = cnName.indexOf("(") > 0 ? cnName
							.substring(0, cnName.indexOf("(")) : ntUser;
							return fullName;
				}
			}

			return ntUser;

		} catch (NamingException e) {
			logger.error("Failed to logon with LDAP auth", e);
			e.printStackTrace();
		}
		return null;
	}

}
