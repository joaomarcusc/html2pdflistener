html2pdflistener
================

Integração do FlyingSaucer com o JSF

AVISO IMPORTANTE
---------------

A partir da versão 1.2.0, esta biblioteca passou a utilizar o iText 4.1, que ainda
possuía uma licença que permitia o uso da mesma em projetos comerciais. Se você utiliza
esta biblioteca em um projeto comercial, precisa utilizar esta nova versão!

Objetivo
--------

O objetivo desta biblioteca é permitir que qualquer página HTML de um projeto
JSF possa ser convertido em PDF para ser mostrado diretamente ao usuário ou
então disponibilizado para outra ação JSF.

Instalando
--------

Adicione o repositório no seu pom.xml

    <repository>
        <id>html2pdflistener.repo</id>
        <name>html2pdflistener.repo</name>
        <url>https://raw.github.com/joaomc/html2pdflistener-repo/master/repository/</url>
    </repository>

Depois, adicione a dependência no seu pom.xml

    <dependency>
        <groupId>br.com.christ.jsf</groupId>
        <artifactId>html2pdflistener</artifactId>
        <version>1.2.1</version>
    </dependency>

Agora, adicione no seu faces-config.xml o listener:

     <lifecycle>
      <phase-listener>br.com.christ.jsf.html2pdf.listener.Html2PDFPhaseListener</phase-listener>
     </lifecycle>

Mostrando um PDF diretamente ao usuário
-------

Caso você queira que uma página específica seja mostrada ao usuário, adicione
as seguintes linhas na sua action, pouco antes do "return":

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        request.setAttribute("gerar_pdf", "1");

No exemplo abaixo, a página resultante do retorno da action será transformada em PDF
e mostrada ao usuário como um arquivo com nome "relatorio.pdf":

    public String gerarRelatorio() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        request.setAttribute("gerar_pdf", "1");
        request.setAttribute("nome_arquivo_pdf", "relatorio.pdf");
        return "relatorio";
    }

Gerando um PDF e repassando-o a outro método
------

Caso você queira, por exemplo, gerar o PDF de uma página e enviá-la via e-mail, 
pode configurar o listener para que ele gere o PDF e repasse os bytes gerados
para uma segunda action:

    public String gerarRelatorioEmail() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        request.setAttribute("gerar_pdf", "1");
        request.setAttribute("nome_arquivo_pdf", "relatorio.pdf");
        request.setAttribute("action_pdf", "#{meuRelatorioMB.enviarEmailRelatorio}");
        return "relatorio";
    }

    public String enviarEmailRelatorio(byte[] bytesPDFRelatorio) {
        // Sua função para manipular o PDF aqui
        return "pagina_resultado";
    }

Neste caso, a página resultante da action gerarRelatorioEmail é transformada em PDF.
Os bytes do PDF são repassados à action enviarEmailRelatorio, e o que o usuário
verá no final é o resultado da última action.

Problemas ao carregar imagens, CSS, etc
------------------------------

Caso o PDF gerado esteja sem a formatação adequada ou sem imagens, provavelmente está
havendo problemas ao carregar as imagens ou CSS. Uma forma de resolver o problema é
fazer o embed de imagens em Base64 e inserir o CSS diretamente na página ao invés de
fazer referências. Você pode também utilizar a funcionalidade de preload de imagens para
evitar que o gerador precise fazer o download durante a geração do PDF. Para isso, insira
antes do "src" da imagem a tag "preload:". Exemplo:

    <img src="preload:/public/resources/images/logo.png" />

Dessa maneira, a imagem será carregada no listener JSF. Isso é necessário caso haja:

 * Problemas ao carregar arquivos de servidor HTTPS
 * Recursos que estiverem em um local com acesso restrito (login necessário)

A partir da versão 1.1.14, o componente irá precarregar automaticamente as imagens e
stylesheets se for detectada uma conexão HTTPS.

A partir da versão 1.1.16, você pode forçar o pré-carregamento de imagens e CSS passando o
atributo de request "preload_resources":

    public String gerarRelatorioEmail() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        request.setAttribute("gerar_pdf", "1");
        request.setAttribute("nome_arquivo_pdf", "relatorio.pdf");
        request.setAttribute("preload_resources", "1");
        return "relatorio";
    }


Problemas de codificação
------------------------------

A partir da versão 1.1.17, a codificação é passada para o Tidy, evitando problemas de acentuação. 

Caso mesmo assim você tenha problemas de codificação, pode informar manualmente a codificação do
PDF gerado:

    public String gerarRelatorioEmail() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        request.setAttribute("gerar_pdf", "1");
        request.setAttribute("encoding", "iso-8859-1");
        return "relatorio";
    }


