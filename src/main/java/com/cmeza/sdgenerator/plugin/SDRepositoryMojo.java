package com.cmeza.sdgenerator.plugin;

import com.cmeza.sdgenerator.support.RepositoryTemplateSupport;
import com.cmeza.sdgenerator.support.ScanningConfigurationSupport;
import com.cmeza.sdgenerator.util.*;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;

/**
 * Created by carlos on 22/04/17.
 */
@Mojo(name = "repositories")
@Execute(phase = LifecyclePhase.COMPILE)
@SuppressWarnings("unused")
public class SDRepositoryMojo extends CommonsMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        SDLogger.configure(getLog());

        this.validateField(Constants.ENTITY_PACKAGE);
        this.validateField(Constants.REPOSITORY_PACKAGE);

        try {
            CustomResourceLoader resourceLoader = new CustomResourceLoader(project);
            resourceLoader = new CustomResourceLoader(project);
            resourceLoader.setPostfix(repositoryPostfix);
            resourceLoader.setOverwrite(overwrite);
            resourceLoader.setFlag(repositoryFlag);
            resourceLoader.setRepositoryFinderBy(repositoryFinderBy);

            String absolutePath = GeneratorUtils.getAbsolutePath(repositoryPackage);
            if (absolutePath == null){
                SDLogger.addError("Could not define the absolute path of the repositories");
                throw new SDMojoException();
            }

            ScanningConfigurationSupport scanningConfigurationSupport = new ScanningConfigurationSupport(entityPackage, onlyAnnotations);

            RepositoryTemplateSupport repositoryTemplateSupport = new RepositoryTemplateSupport(resourceLoader);
            repositoryTemplateSupport.initializeCreation(absolutePath, repositoryPackage, scanningConfigurationSupport.getCandidates(resourceLoader));

            SDLogger.printGeneratedTables(true);

        } catch (Exception e) {
            SDLogger.addError(e.getMessage());
            throw new SDMojoException(e.getMessage(), e);
        }
    }
    
}
