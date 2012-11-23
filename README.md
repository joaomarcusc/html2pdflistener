html2pdflistener
================

Integração do FlyingSaucer com o JSF

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
        <url>https://github.com/joaomc/html2pdflistener-repo/raw/master</url>
    </repository>

Depois, adicione a dependência no seu pom.xml

    <dependency>
        <groupId>br.com.christ.jsf</groupId>
        <artifactId>html2pdflistener</artifactId>
        <version>1.0</version>
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



