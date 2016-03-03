package com.dabsquared.gitlabjenkins.trigger;

import com.dabsquared.gitlabjenkins.GitLabPushTrigger;
import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.AntPathMatcher;

/**
 * @author Robin Müller
 */
public enum BranchFilterType {
    All{
        @Override
        public boolean isBranchAllowed(String branchName, GitLabPushTrigger trigger) {
            return true;
        }
    },
    NameBasedFilter {
        @Override
        public boolean isBranchAllowed(String branchName, GitLabPushTrigger trigger) {
            return hasNoBranchSpecs(trigger) || (isBranchNotExcluded(branchName, trigger) && isBranchIncluded(branchName, trigger));
        }

        private boolean hasNoBranchSpecs(GitLabPushTrigger trigger) {
            return StringUtils.isEmpty(trigger.getExcludeBranchesSpec()) && StringUtils.isEmpty(trigger.getIncludeBranchesSpec());
        }

        private boolean isBranchNotExcluded(String branchName, GitLabPushTrigger trigger) {
            AntPathMatcher matcher = new AntPathMatcher();
            for (String excludePattern : Splitter.on(',').omitEmptyStrings().trimResults().split(trigger.getExcludeBranchesSpec())) {
                if (matcher.match(excludePattern, branchName)) {
                    return false;
                }
            }
            return true;
        }

        private boolean isBranchIncluded(String branchName, GitLabPushTrigger trigger) {
            AntPathMatcher matcher = new AntPathMatcher();
            for (String includePattern : Splitter.on(',').omitEmptyStrings().trimResults().split(trigger.getIncludeBranchesSpec())) {
                if (matcher.match(includePattern, branchName)) {
                    return true;
                }
            }
            return StringUtils.isEmpty(trigger.getIncludeBranchesSpec());
        }
    },
    RegexBasedFilter {
        @Override
        public boolean isBranchAllowed(String branchName, GitLabPushTrigger trigger) {
            String regex = trigger.getTargetBranchRegex();
            return StringUtils.isEmpty(branchName) || StringUtils.isEmpty(regex) || branchName.matches(regex);
        }
    };

    public abstract boolean isBranchAllowed(String branchName, GitLabPushTrigger trigger);
}
