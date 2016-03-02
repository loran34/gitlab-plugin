package com.dabsquared.gitlabjenkins.cause;

import com.dabsquared.gitlabjenkins.model.WebHook;
import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildVariableContributor;

import java.util.Map;

/**
 * @author Robin Müller
 */
@Extension
public class GitLabBuildVariableContributor extends BuildVariableContributor {
    @Override
    @SuppressWarnings("unchecked")
    public void buildVariablesFor(AbstractBuild build, Map<String, String> variables) {
        GitLabWebHookCause<WebHook> cause = (GitLabWebHookCause<WebHook>) build.getCause(GitLabWebHookCause.class);
        if (cause != null) {
            variables.putAll(cause.getBuildVariables());
        }
    }
}
