package ps.hell.ml.statistics;

/**
 * @author Orisun
 * date 2011-10-20
 */
import java.util.ArrayList;
import java.util.Collections;

public class HMM {
    ArrayList<String> state;
    ArrayList<String> observation;
    double[] P;
    double[][] A;
    double[][] B;
    int M;
    int N;

    HMM() {
        // 天气只有两种状态：晴和兩。每天只有一种天气。
        state = new ArrayList<String>();
        state.add("sunny");
        state.add("rain");
        M = state.size();
        // 活动只有三种：散步、看书、做清洁。每天只做一种活动。
        observation = new ArrayList<String>();
        observation.add("walk");
        observation.add("read");
        observation.add("clear");
        N = observation.size();
        // 初始状态矩阵。某天晴的概率是0.6，兩的概率是0.4。
        P = new double[] { 0.6, 0.4 };
        // 状态转移矩阵。
        A = new double[M][];
        // 从晴转晴的概率是0.7，从晴转兩的概率是0.3
        A[0] = new double[] { 0.7, 0.3 };
        // 从兩转晴的概率是0.4，从兩转兩的概率是0.6
        A[1] = new double[] { 0.4, 0.6 };
        // 混淆矩阵
        B = new double[M][];
        // 天气晴时，散步的概率是0.4，看书的概率是0.3，做清洁的概率是0.3。
        B[0] = new double[] { 0.4, 0.3, 0.3 };
        // 天气兩时，散步的概率是0.1，看书的概率是0.4，做清洁的概率是0.5。
        B[1] = new double[] { 0.1, 0.4, 0.5 };
    }

    public double forward(ArrayList<String> observe) {
        double rect = 0.0;
        int LEN = observe.size();
        double[][] Q = new double[LEN][];
        // 第一天计算，状态的初始概率，乘上隐藏状态到观察状态的条件概率。
        Q[0] = new double[M];
        for (int j = 0; j < M; j++) {
            Q[0][j] = P[j] * B[j][observation.indexOf(observe.get(0))];
        }
        // 第一天以后的计算，首先从前一天的每个状态，转移到当前状态的概率求和，然后乘上隐藏状态到观察状态的条件概率。
        for (int i = 1; i < LEN; i++) {
            Q[i] = new double[M];
            for (int j = 0; j < M; j++) {
                double sum = 0.0;
                for (int k = 0; k < M; k++) {
                    sum += Q[i - 1][k] * A[k][j];
                }
                Q[i][j] = sum * B[j][observation.indexOf(observe.get(i))];
            }
        }
        for (int i = 0; i < M; i++)
            rect += Q[LEN - 1][i];
        return rect;
    }

    public ArrayList<String> viterbi(ArrayList<String> observe) {
        ArrayList<String> sta = new ArrayList<String>();
        int LEN = observe.size();
        double[][] Q = new double[LEN][];
        int[][] Path = new int[LEN][];
        // 第一天计算，状态的初始概率，乘上隐藏状态到观察状态的条件概率。
        Q[0] = new double[M];
        Path[0] = new int[M];
        for (int j = 0; j < M; j++) {
            Q[0][j] = P[j] * B[j][observation.indexOf(observe.get(0))];
            Path[0][j] = -1;
        }
        //第一天以后的计算，首先从前一天的每个状态，转移到当前状态的概率的最大值，然后乘上隐藏状态到观察状态的条件概率。
        for (int i = 1; i < LEN; i++) {
            Q[i] = new double[M];
            Path[i] = new int[M];
            for (int j = 0; j < M; j++) {
                double max = 0.0;
                int index = 0;
                for (int k = 0; k < M; k++) {
                    if (Q[i - 1][k] * A[k][j] > max) {
                        max = Q[i - 1][k] * A[k][j];
                        index = k;
                    }
                }
                Q[i][j] = max * B[j][observation.indexOf(observe.get(i))];
                Path[i][j] = index;
            }
        }
        // 找到最后一天天气呈现哪种状态的概率最大
        double max = 0;
        int index = 0;
        for (int i = 0; i < M; i++) {
            if (Q[LEN - 1][i] > max) {
                max = Q[LEN - 1][i];
                index = i;
            }
        }
        System.out.println("出现最可能的隐藏序列的概率是"+max);
        sta.add(state.get(index));
        // 动态规划，逆推回去各天出现什么天气概率最大
        for (int i = LEN - 1; i > 0; i--) {
            index = Path[i][index];
            sta.add(state.get(index));
        }
        // 把状态序列再顺过来
        Collections.reverse(sta);
        return sta;
    }
    
    public static void main(String[] args) {
        HMM hmm = new HMM();

        ArrayList<String> obs_seq = new ArrayList<String>();
        obs_seq.add("read");
        obs_seq.add("clear");
        obs_seq.add("walk");
        obs_seq.add("clear");
        obs_seq.add("walk");

        double ratio = hmm.forward(obs_seq);
        System.out.println(ratio);

        ArrayList<String> sta_seq = hmm.viterbi(obs_seq);
        System.out.println(sta_seq.toString());
    }
}