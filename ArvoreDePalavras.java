/*
 * Trabalho de Resolução de Problemas Estruturados em Computação
 * Aluno: André Luís Scharaiber Alves - BCC
 * Professor: Andrey Cabral Meira
 * Implementação de Árvore Trie
 */

class NoDaArvore {
    char letra;
    NoDaArvore[] proximos;
    boolean ehFim;

    public NoDaArvore(char c) {
        this.letra = c;
        this.proximos = new NoDaArvore[26];
        this.ehFim = false;
        for(int x = 0; x < 26; x++) {
            proximos[x] = null;
        }
    }
}

public class ArvoreDePalavras {
    private final NoDaArvore base;
    private int contador;

    public ArvoreDePalavras() {
        this.base = new NoDaArvore('0');
        this.contador = 0;
    }

    private int charIndex(char c) {
        return c - 'a';
    }

    public void add(String palavra) {
        if(palavra == null) return;

        NoDaArvore atual = base;
        int tam = palavra.length();

        for(int i = 0; i < tam; i++) {
            char l = palavra.charAt(i);
            int idx = charIndex(l);

            if(atual.proximos[idx] == null) {
                atual.proximos[idx] = new NoDaArvore(l);
            }

            atual = atual.proximos[idx];
        }

        atual.ehFim = true;
        contador++;
    }

    public boolean buscar(String palavra) {
        if(palavra == null) return false;

        NoDaArvore atual = base;
        int tamanho = palavra.length();

        for(int pos = 0; pos < tamanho; pos++) {
            char letra = palavra.charAt(pos);
            int indice = charIndex(letra);

            if(atual.proximos[indice] == null) {
                return false;
            }

            atual = atual.proximos[indice];
        }

        boolean resultado = atual.ehFim;
        System.out.println("Busca por " + palavra + ": " + resultado);
        return resultado;
    }

    public boolean deletar(String palavra) {
        if(!buscar(palavra)) {
            System.out.println("Não tem " + palavra);
            return false;
        }

        System.out.println("Tentando deletar: " + palavra);

        boolean ok = deletarRec(base, palavra, 0);
        if(ok) {
            contador--;
            System.out.println("Deletad:o " + palavra);
        } else {
            System.out.println("Erro");
        }
        return ok;
    }

    private boolean deletarRec(NoDaArvore no, String palavra, int nivel) {
        if(no == null) return false;

        if(nivel == palavra.length()) {
            if(no.ehFim) {
                no.ehFim = false;
                return true;
            }
            return false;
        }

        char l = palavra.charAt(nivel);
        int pos = charIndex(l);
        NoDaArvore filho = no.proximos[pos];

        if(deletarRec(filho, palavra, nivel + 1)) {
            if(filho != null && !filho.ehFim) {
                boolean temFilhos = false;
                for(int j = 0; j < 26; j++) {
                    if(filho.proximos[j] != null) {
                        temFilhos = true;
                        break;
                    }
                }

                if (!temFilhos) {
                    no.proximos[pos] = null;
                }
            }
            return true;
        }

        return false;
    }

    public boolean temPrefixo(String prefixo) {
        if(prefixo == null) return false;

        NoDaArvore atual = base;
        int compr = prefixo.length();

        for(int i = 0; i < compr; i++) {
            char c = prefixo.charAt(i);
            int idx = charIndex(c);

            if(atual.proximos[idx] == null) {
                return false;
            }

            atual = atual.proximos[idx];
        }

        return true;
    }

    public void mostrar(String prefixo) {
        NoDaArvore atual = base;

        if(!temPrefixo(prefixo)) {
            System.out.println("Zero resultados pra " + prefixo);
            return;
        }

        for(int i = 0; i < prefixo.length(); i++) {
            char c = prefixo.charAt(i);
            atual = atual.proximos[charIndex(c)];
        }

        listarPalavras(atual, prefixo);
    }

    private void listarPalavras(NoDaArvore no, String prefixoAtual) {
        if(no == null) return;

        if(no.ehFim) {
            System.out.println("-> " + prefixoAtual);
        }

        for(int idx = 0; idx < 26; idx++) {
            if(no.proximos[idx] != null) {
                char novaLetra = (char)('a' + idx);
                listarPalavras(no.proximos[idx], prefixoAtual + novaLetra);
            }
        }
    }

    public void info() {
        System.out.println("Contador: " + contador + " palavras");
    }

    public static void main(String[] args) {
        ArvoreDePalavras arvore = new ArvoreDePalavras();

        arvore.add("casa");
        arvore.add("casamento");
        arvore.add("casaco");
        arvore.add("carro");
        arvore.add("dado");

        System.out.println("Testes");
        arvore.buscar("casa");
        arvore.buscar("cas");
        arvore.buscar("carro");
        System.out.println("Prefixo 'cas': " + arvore.temPrefixo("cas"));
        System.out.println("Prefixo 'da': " + arvore.temPrefixo("da"));

        System.out.println("Listando");
        arvore.mostrar("cas");
        System.out.println("Deletando");
        arvore.deletar("casamento");
        System.out.println("Listando");
        arvore.mostrar("cas");
        arvore.info();

    }
}