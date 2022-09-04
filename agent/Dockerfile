# Primerily to run newman tests, this agent also includes other shell utilities.
# docker build --tag agent . ; docker run --interactive --tty agent sh
#FROM postman/newman:alpine
#FROM node:16-alpine
#FROM alpine
FROM ubuntu:latest

MAINTAINER Polycode Limited https://polycode.co.uk/

#ARG NEWMAN_VERSION

# Set environment variables
#ENV LC_ALL="en_US.UTF-8" LANG="en_US.UTF-8" LANGUAGE="en_US.UTF-8" ALPINE_NODE_REPO="oznu/alpine-node"

# Bash
#RUN \
#  apk add --update --no-cache \
#	 bash \
#    ;

# Node
#RUN \
#  apk add --update --no-cache \
#	 nodejs \
#    npm \
#	 ;

# Utilities
#RUN \
#  apk add --update --no-cache \
#    curl \
#    jq \
#    make \
#    openssl \
#    unzip \
#    wget \
#    zip \
#    ;

# Bail out early if NODE_VERSION is not provided
#RUN if [ ! $(echo $NEWMAN_VERSION | grep -oE "^[0-9]+\.[0-9]+\.[0-9]+$") ]; then \
#        echo "\033[0;31mA valid semver Newman version is required in the NEWMAN_VERSION build-arg\033[0m"; \
#        exit 1; \
#    fi && \
#    # Install Newman globally
#    npm install --global newman@${NEWMAN_VERSION};
#RUN \
#	npm install --global newman \
#  	;

# SSH server
RUN apt-get update && apt-get install -y openssh-server
RUN mkdir /var/run/sshd
#RUN \
#  apk add --update --no-cache \
#	 openssh-client \
#    openssh-server \
#  	 ;
#RUN \
#  adduser -S -s /bin/sh newuser \
#    ;
EXPOSE 22

# User for remote access and script execution
#WORKDIR /home/runner

# SSH access
#RUN sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN useradd -d /home/dashboard -m -U -s /bin/sh dashboard
#USER dashboard
COPY ./ssh/id_rsa_dashboard.pub /home/dashboard/id_rsa_dashboard.pub
RUN \
    mkdir -p /home/dashboard/.ssh \
    && mv /home/dashboard/id_rsa_dashboard.pub /home/dashboard/.ssh/authorized_keys \
    && chown -R dashboard:dashboard /home/dashboard/.ssh \
    && chmod u+rwx,go-rwx           /home/dashboard/.ssh \
    && chmod u+rw,go-rwx            /home/dashboard/.ssh/authorized_keys \
    ;
#        && cat /home/dashboard/id_rsa_dashboard.pub >> /home/dashboard/.ssh/authorized_keys \
#    ; rm -f /home/dashboard/id_rsa_dashboard.pub \
#    ; ls -lart /home/dashboard/.ssh \
#    ;
#RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd
#RUN \
#  apk add --update --no-cache \
#	 openssh-client \
#    openssh-server \
#  	 ;
#RUN \
#  adduser -S -s /bin/sh newuser \
#    ;
#EXPOSE 22
#USER root

#EXPOSE 22

# Set workdir to /etc/newman
# It seems this is not needed: mkdir -p /etc/newman && \
# When running the image, mount the directory containing your collection to this location
#
# docker run -v <path to collections directory>:/etc/newman ...
#
# In case you mount your collections directory to a different location, you will need to give absolute paths to any
# collection, environment files you want to pass to newman, and if you want newman reports to be saved to your disk.
# Or you can change the workdir by using the -w or --workdir flag
#WORKDIR "/etc/newman"

#COPY ./postman/api-tests.postman_collection.json /etc/newman/.
#COPY ./postman/docker.postman_environment.json /etc/newman/.
#COPY ./postman/newmanTest.postman_collection.json /etc/newman/.

# Without this the container will exit immediately because it inherits ENTRYPOINT ["newman"] from the base image
ENTRYPOINT []
#ENTRYPOINT ["tail", "-f", "/dev/null"]

#CMD ["bash"]

CMD ["/usr/sbin/sshd", "-D"]

#ENV HOME /root

#WORKDIR /root

#CMD ["bash"]
