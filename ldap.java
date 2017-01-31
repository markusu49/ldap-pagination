package ldap;

import java.util.Hashtable;
import java.io.IOException;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;

public class App {

	public static void main(String[] args) {
		Hashtable<String, Object> env = new Hashtable<String, Object>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://server/o=myorg");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "cn=admin,o=myorg");
		env.put(Context.SECURITY_CREDENTIALS, "password");

		try {
			LdapContext ctx = new InitialLdapContext(env, null);

			NamingEnumeration results = null;
			SearchControls sc = new SearchControls();

			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

			results = ctx.search("", "(objectclass=*)", sc);

			while (results != null && results.hasMore()) {
				SearchResult entry = (SearchResult) results.next();
				System.out.println(entry.getName()); // prints all entries
			}

			System.out.println("==========");

			int pageSize = 3;
			ctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.NONCRITICAL) });
			results = ctx.search("", "(objectclass=*)", sc);

			while (results != null && results.hasMore()) {
				SearchResult entry = (SearchResult) results.next();
				System.out.println(entry.getName()); // prints 2 entries
			}

			System.out.println("==========");

			ctx.setRequestControls(null);
			results = ctx.search("", "(objectclass=*)", sc);

			while (results != null && results.hasMore()) {
				SearchResult entry = (SearchResult) results.next();
				System.out.println(entry.getName()); // prints all entries
			}

			ctx.close();
		} catch (NamingException e) {
			System.err.println("PagedSearch failed.");
			e.printStackTrace();
		} catch (IOException ie) {
			System.err.println("PagedSearch failed.");
			ie.printStackTrace();
		}
	}

}
