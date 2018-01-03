/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author hebik
 */
public class Knot {

    public static String knot(String data) {
        int[] elements = IntStream.range(0, 256).toArray();
        byte[] lengths = new byte[data.getBytes().length + 5];
        lengths[lengths.length - 5] = 17;
        lengths[lengths.length - 4] = 31;
        lengths[lengths.length - 3] = 73;
        lengths[lengths.length - 2] = 47;
        lengths[lengths.length - 1] = 23;
        System.arraycopy(data.getBytes(), 0, lengths, 0, data.getBytes().length);

        int position = 0;
        int skip = 0;
        for (int i = 0; i < 64; i++) {
            for (int length : lengths) {
                reverse(elements, position, length);
                position = (position + length + skip) % elements.length;
                skip++;
            }
        }

        StringBuilder sb = new StringBuilder();
        List<Integer> elem = Arrays.stream(elements).boxed().collect(Collectors.toList());
        IntStream.range(0, (elem.size() + 16 - 1) / 16).mapToObj(i -> elem.subList(i * 16, Math.min(16 * (i + 1), elem.size()))).forEach(i -> sb.append(String.format("%02x", xor(i))));

        return sb.toString();
    }

    public static Integer xor(List<Integer> l) {
        return l.stream().mapToInt(i -> i).reduce(0, (a, b) -> a ^ b);
    }

    public static void reverse(int[] elements, int start, int length) {
        for (int i = 0; i < length / 2; i++) {
            int temp = elements[(start + i) % elements.length];
            elements[(start + i) % elements.length] = elements[(start + length - i - 1) % elements.length];
            elements[(start + length - i - 1) % elements.length] = temp;
        }
    }
}
