package jira.test.JiRATest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import com.atlassian.jira.rest.client.IssueRestClient;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.SearchRestClient;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Comment;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.Project;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.domain.input.IssueInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

/**
 * Hello world!
 *
 */
public class App {
		
	public static void main(String[] args) throws URISyntaxException {
		System.out.println("Starting the code ...");
		System.out.println("---------------------");
		
		final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		
		/*... in test ...*/
		//final URI jiraServerUri = new URI("http://localhost:28080/");
		//final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "test1", "test123");
		
		/*... in production ...*/
		final URI jiraServerUri = new URI("http://localhost/");
		
		final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "user", "pass");
		final SearchRestClient searchClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "user", "pass").getSearchClient();
		final IssueRestClient issueClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "user", "pass").getIssueClient();
		
		/*... test project ...*/
		//final Project issue = restClient.getProjectClient().getProject("TEST").claim();
		//System.out.println(issue.getName().toString());
		
		/*... test issue ...*/
		//final Issue issue = restClient.getIssueClient().getIssue("MO-1233").claim();
		//System.out.println(issue);
		
		/*... Searching & listing all issues ...*/		
		String jql = "project = MO AND description ~ TEST";
		SearchResult results = searchClient.searchJql(jql).claim();
		
		Iterable<BasicIssue> basicIssues = null;
	   	ArrayList<Issue> issues = new ArrayList<Issue>();
	   	
	   	basicIssues = results.getIssues();
		
	   	System.out.println("total: " + results.getTotal());
	   	
		for(Iterator<BasicIssue> i = basicIssues.iterator();i.hasNext();) {
			BasicIssue b = (BasicIssue) i.next();
			issues.add(issueClient.getIssue(b.getKey()).claim());
	     }
	   	
		/*... Edit Issue (add comment) ...*/
		Promise<Issue> issuePromise = issueClient.getIssue("issue-id");
		Issue issue = issuePromise.claim();
		
		issueClient.addComment(issue.getCommentsUri(), Comment.valueOf("This is Test Comment"));
		
		/*
        Promise<Issue> issuePromise = issueClient.getIssue(comment.getId());
        Issue issue = issuePromise.claim();
        
        issueClient.addComment(issue.getCommentsUri(), Comment.valueOf(comment.getComment()));*/
	   	
		System.out.println("--------------------");
	   	System.out.println("... End of the code.");
	}
}
