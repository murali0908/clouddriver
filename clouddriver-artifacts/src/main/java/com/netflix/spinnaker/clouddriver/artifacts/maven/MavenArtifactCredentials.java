/*
 * Copyright 2018 Pivotal, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.artifacts.maven;

import com.netflix.spinnaker.clouddriver.artifacts.ivy.IvyArtifactAccount;
import com.netflix.spinnaker.clouddriver.artifacts.ivy.IvyArtifactCredentials;
import com.netflix.spinnaker.clouddriver.artifacts.ivy.settings.IBiblioResolver;
import com.netflix.spinnaker.clouddriver.artifacts.ivy.settings.IvySettings;
import com.netflix.spinnaker.clouddriver.artifacts.ivy.settings.Resolvers;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Data
public class MavenArtifactCredentials extends IvyArtifactCredentials {
  private final List<String> types = Collections.singletonList("maven/file");

  public MavenArtifactCredentials(MavenArtifactAccount account) {
    super(toIvyAccount(account));
  }

  public MavenArtifactCredentials(MavenArtifactAccount account, Supplier<Path> cacheBuilder) {
    super(toIvyAccount(account), cacheBuilder);
  }

  private static IvyArtifactAccount toIvyAccount(MavenArtifactAccount maven) {
    IvyArtifactAccount ivy = new IvyArtifactAccount();

    IBiblioResolver mavenResolver = new IBiblioResolver();
    mavenResolver.setName("maven");
    mavenResolver.setM2compatible(true);
    mavenResolver.setUsepoms(true);
    mavenResolver.setUseMavenMetadata(true);
    mavenResolver.setRoot(maven.getRepositoryUrl());

    Resolvers resolvers = new Resolvers();
    resolvers.setIbiblio(Collections.singletonList(mavenResolver));

    IvySettings settings = new IvySettings();
    settings.setResolvers(resolvers);

    ivy.setSettings(settings);

    return ivy;
  }
}
