package com.soap.githubapi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*

API 문서 : https://docs.github.com/en/rest/reference/reactions#list-reactions-for-an-issue-comment

https://api.github.com/repos/whiteship/live-study
https://api.github.com/repos/whiteship/live-study/issues/1
https://api.github.com/repos/whiteship/live-study/issues/1/comments

header : Accept : application/vnd.github.squirrel-girl-preview+jso
https://api.github.com/repos/whiteship/live-study/issues/1/reactions
 */
@SpringBootApplication
public class GithubapiApplication {

	//personal token need to secret
	private static final String MY_PERSONAL_TOKEN = "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";

	public static void main(String[] args) throws IOException {
		GitHub github = new GitHubBuilder().withOAuthToken(MY_PERSONAL_TOKEN).build();

		//Repository 연결
		GHRepository repo = github.getRepository("whiteship/live-study");

		//IssueState ALL, OPEN, CLOSED
		List<GHIssue> issues = repo.getIssues(GHIssueState.ALL);

		//System.out.println(issues);

		//Map<String, Integer> participant = new HashMap<>();

	//	Map<String, String> topic = new HashMap<>();

		for (GHIssue issue : issues) {
			//System.out.println(issue.listReactions().toList());

			//System.out.println(issue.getUrl());
			URL url = issue.getUrl();
			String urlToStr = url.toString();
			String key = urlToStr.substring(urlToStr.lastIndexOf("/")+1, urlToStr.length());
			if(Integer.parseInt(key) <= 15){
				System.out.println(urlToStr);
				//topic.put(key, url.toString());
				//System.out.println(issue.getComments().size());
				System.out.println(issue.getComments().size());
				for (GHIssueComment comment : issue.getComments()) {
					//System.out.println(comment.getId());
					//System.out.println(comment.getUser().getName());
					System.out.println(comment.getUser().getLogin());
					System.out.println(comment.listReactions().toList());


					// 이거다
					// https://api.github.com/repos/whiteship/live-study/issues/comments/787469111/reactions

//					comment.getUser().get
//					comment.getId();
//					comment.getUser().getLogin();
//					comment.getBody();
				}

			}
		}

		//1-18개 이슈
//		for (GHIssue issue : issues) {
//			Set<String> onlyOneParticipant = new HashSet<>();
//
//			//댓글 한개 이상 단 경우 유저이름 중복 제거
//			for (GHIssueComment comment : issue.getComments()) {
//				onlyOneParticipant.add(comment.getUser().getName());
//			}

			//카운트 증가해주기
//			for (String name : onlyOneParticipant) {
//				if(participant.containsKey(name)){
//					participant.replace(name,participant.get(name)+1);
//					continue;
//				}
//				participant.put(name,1);
//			}
		//}
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		//참여율 출력
//		for(String name : participant.keySet()){
//			double rate = (double)(participant.get(name) * 100) / issues.size();
//			bw.write("name : " + name);
//			bw.write(", Participation Rate : "+String.format("%.2f",rate)+"%");
//			bw.newLine();
//		}
		bw.close();
	}


}
