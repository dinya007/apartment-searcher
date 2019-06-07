#!/usr/bin/env bash
git pull && ./gradlew clean build && java -Dspring.profiles.active=production -jar build/libs/search-apartments-1.0.0.jar