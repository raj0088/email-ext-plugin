package hudson.plugins.emailext.plugins;

import hudson.model.FreeStyleProject;
import hudson.plugins.emailext.plugins.recipients.DevelopersRecipientProvider;
import hudson.plugins.emailext.plugins.recipients.ListRecipientProvider;
import hudson.plugins.emailext.plugins.recipients.RequesterRecipientProvider;
import org.hamcrest.CoreMatchers;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class RecipientProviderTest {

    @Rule public JenkinsRule j = new JenkinsRule();

    @Test
    public void allSupporting() {
        List<RecipientProviderDescriptor> descriptors = RecipientProvider.allSupporting(WorkflowJob.class);
        assertThat(descriptors, CoreMatchers.hasItem(CoreMatchers.isA(DevelopersRecipientProvider.DescriptorImpl.class)));
        assertThat(descriptors, CoreMatchers.not(CoreMatchers.hasItem(CoreMatchers.isA(ListRecipientProvider.DescriptorImpl.class))));

        descriptors = RecipientProvider.allSupporting(FreeStyleProject.class);
        assertThat(descriptors, CoreMatchers.hasItem(CoreMatchers.isA(DevelopersRecipientProvider.DescriptorImpl.class)));
        assertThat(descriptors, CoreMatchers.hasItem(CoreMatchers.isA(ListRecipientProvider.DescriptorImpl.class)));
    }

    @Test
    public void checkAllSupport() {
        RecipientProvider.checkAllSupport(Arrays.asList(new RequesterRecipientProvider(),
                new DevelopersRecipientProvider()), WorkflowJob.class);
        try {
            RecipientProvider.checkAllSupport(Arrays.asList(new RequesterRecipientProvider(),
                    new ListRecipientProvider()), WorkflowJob.class);
            fail("Expected to throw exception");
        } catch (IllegalArgumentException ex) {
            assertEquals(MessageFormat.format("The following recipient providers do not support {0} {1}",
                    WorkflowJob.class.getName(), ListRecipientProvider.class.getName()), ex.getMessage());
        }
    }

}
