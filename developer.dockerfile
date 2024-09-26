FROM ubuntu:20.04

RUN apt-get update
RUN apt update
RUN apt -y upgrade
RUN apt-get -y install make curl git make gpg gcc vim
RUN git clone https://github.com/asdf-vm/asdf.git ~/.asdf --branch v0.14.1
RUN echo . "\$HOME/.asdf/asdf.sh" | sed s/\\\\\\$/$/ >> ~/.bashrc
WORKDIR /root/
ADD ./developer/image/default_installs.sh ./default_installs.sh
RUN chmod +x ./default_installs.sh
RUN ["/bin/bash", "./default_installs.sh"]

