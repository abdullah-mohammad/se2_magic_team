FROM gradle:6.7.1-jdk11
COPY settings.gradle build.gradle ./
COPY src ./src/
CMD ["gradle","bootRun"]